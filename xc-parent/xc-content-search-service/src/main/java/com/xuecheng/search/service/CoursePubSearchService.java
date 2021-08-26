package com.xuecheng.search.service;

import com.xuecheng.api.search.model.dto.CoursePubIndexDTO;
import com.xuecheng.api.search.model.qo.QueryCoursePubModel;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;

import java.io.IOException;

/**
 *  课程搜索服务层
 */
public interface CoursePubSearchService {
    /**
     * 根据条件分页查询
     * @param pageRequestParams {@link PageRequestParams} 分页数据封装对象
     * @param queryModel {@link QueryCoursePubModel} 条件查询对象
     * @return PageVO 分页封装数据
     */
    PageVO<CoursePubIndexDTO> queryCoursePubIndex(PageRequestParams pageRequestParams, QueryCoursePubModel queryModel) throws IOException;
    /**
     * 根据id查询课程发布索引数据
     * @param coursePubId
     * @return
     */
    CoursePubIndexDTO getCoursePubIndexById(Long coursePubId);

    /**
     * 根据id查询课程发布索引数据--远程调用
     * @param coursePubId
     * @return
     */
    RestResponse<CoursePubIndexDTO> getCoursePubIndexById4s(Long coursePubId);
}
