package com.xuecheng.search.service.impl;


import com.alibaba.fastjson.JSON;
import com.xuecheng.api.search.model.dto.CoursePubIndexDTO;
import com.xuecheng.api.search.model.qo.QueryCoursePubModel;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.JsonUtil;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.search.common.constant.ContentSearchErrorCode;
import com.xuecheng.search.convert.CoursePubIndexConvert;
import com.xuecheng.search.model.CoursePubIndex;
import com.xuecheng.search.service.CoursePubSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 课程搜索服务实现层(es原始Api实现)
 */
@Slf4j
@Service
public class CoursePubSearchServiceImpl implements CoursePubSearchService {

    @Value("${xuecheng.elasticsearch.course.index}")
    private String indexName;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public PageVO<CoursePubIndexDTO> queryCoursePubIndex(PageRequestParams pageParams, QueryCoursePubModel queryModel) throws IOException {
        //创建SearchRequest对象
        SearchRequest request = new SearchRequest("xc_course");
        //准备DSL
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //关键字搜索,match查询,放入must(必须条件)中
        if (StringUtils.isEmpty(queryModel.getKeyword())) {
            //关键字为空,查询所有
            request.source().query(boolQuery.must(QueryBuilders.matchAllQuery()));

        } else {
            request.source().query(boolQuery.must(QueryBuilders.matchQuery("name", queryModel.getKeyword())));
        }
        //分类搜索,mt,st
        if (!StringUtils.isEmpty(queryModel.getMt())) {
            request.source().query(boolQuery.filter(QueryBuilders.termQuery("mt", queryModel.getMt())));
        }
        if (!StringUtils.isEmpty(queryModel.getSt())) {
            request.source().query(boolQuery.filter(QueryBuilders.termQuery("st", queryModel.getSt())));
        }
        //难度等级查询
        if (!StringUtils.isEmpty(queryModel.getGrade())) {
            request.source().query(boolQuery.filter(QueryBuilders.termQuery("grade", queryModel.getGrade())));

        }
        //分页
        Integer pageNo = pageParams.getPageNo().intValue();
        Integer pageSize = pageParams.getPageSize();
        request.source().from((pageNo - 1) * pageSize).size(pageSize);
//    排序（作业）
        String sortFiled = queryModel.getSortFiled();
        if (!StringUtils.isEmpty(sortFiled)) {
            if (sortFiled.equals("最新")) {
                request.source().sort("create_date", SortOrder.DESC);
            }
//            if (sortFiled.equals("热评")) {
//                request.source().sort("create_date", SortOrder.DESC);
//            }
        }
        //高亮
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return handleResponse(response, pageParams);

    }

    @Override
    public CoursePubIndexDTO getCoursePubIndexById(Long coursePubId) {
        // 1.判断关键数据
        ExceptionCast.cast(ObjectUtils.isEmpty(coursePubId), CommonErrorCode.E_100101);
        //2.创建es得请求对象
        GetRequest request = new GetRequest(indexName, coursePubId + "");
        //3.根据查询对象获得响应数据
        GetResponse response = null;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询索引数据失败：{}", e.getMessage());
            ExceptionCast.cast(ContentSearchErrorCode.E_150002);
        }
        //4.解析结果获得课程发布索引
        String responseSourceAsString = response.getSourceAsString();
        CoursePubIndexDTO dto = null;
        // 返回结果数据
        if (StringUtil.isBlank(responseSourceAsString)) {
            dto = new CoursePubIndexDTO();
        } else {
            //5.封装
            dto = JSON.parseObject(responseSourceAsString, CoursePubIndexDTO.class);

        }
        return dto;
    }

    @Override
    public RestResponse<CoursePubIndexDTO> getCoursePubIndexById4s(Long coursePubId) {
        // 1.判断关键数据
        if (ObjectUtils.isEmpty(coursePubId)) {
            return RestResponse.validfail(CommonErrorCode.E_100101);
        }

        // 2.从索引库查询数据
        String coursePubJsonString = null;
        try {
            GetRequest request = new GetRequest(indexName, coursePubId.toString());

            GetResponse response = client.get(request, RequestOptions.DEFAULT);


            coursePubJsonString = response.getSourceAsString();
        } catch (IOException e) {
            log.error("查询索引数据失败：{}", e.getMessage());
            return RestResponse.validfail(ContentSearchErrorCode.E_150002);
        }


        // 3.返回结果数据
        if (StringUtil.isBlank(coursePubJsonString)) {
            return RestResponse.validfail(ContentSearchErrorCode.E_150003);
        } else {
            CoursePubIndexDTO dto = JsonUtil.jsonToObject(coursePubJsonString, CoursePubIndexDTO.class);
            return RestResponse.success(dto);
        }

    }


    /*
     *   3.解析结果数据并封装PageVO中
     *       获得大Hits
     *       从大Hits获得总条数
     *       从大Hits获得小Htis数组
     *       遍历小Hits
     *       从小Hist中获得
     *           文档id
     *           文档的源数据_source
     *           文档的高亮
     * */
    private PageVO<CoursePubIndexDTO> handleResponse(SearchResponse response, PageRequestParams pageParams) {
        SearchHits searchHits = response.getHits();
        //查询总条数
        long total = searchHits.getTotalHits().value;
        //查询结果遍历
        SearchHit[] hits = searchHits.getHits();
        List<CoursePubIndex> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            //获取source
            String json = hit.getSourceAsString();
            //将json反序列化
            CoursePubIndex coursePubIndex = JSON.parseObject(json, CoursePubIndex.class);
            //处理高亮
            Map<String, HighlightField> map = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(map)) {
                //获取高亮字段结果
                HighlightField highlightField = map.get("name");
                if (!ObjectUtils.isEmpty(highlightField)) {
                    //取出高亮结果数组中的第一个,即为课程发布名称
                    String name = highlightField.getFragments()[0].toString();
                    //覆盖非高亮结果
                    coursePubIndex.setName(name);
                }
            }
            //放入list集合
            list.add(coursePubIndex);
        }
        //将indexs转dtos
        List<CoursePubIndexDTO> dtos = CoursePubIndexConvert.INSTANCE.indexs2dtos(list);
        //返回pageVO
        return new PageVO<CoursePubIndexDTO>(dtos, total, pageParams.getPageNo(), pageParams.getPageSize());
    }

}