package com.xuecheng.media.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.agent.ContentApiAgent;
import com.xuecheng.api.media.model.dto.MediaAuditDTO;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.api.media.model.vo.MediaAuditVO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.common.AuditEnum;
import com.xuecheng.common.enums.common.CommonEnum;
import com.xuecheng.common.enums.common.ResourceTypeEnum;
import com.xuecheng.common.enums.content.CourseAuditEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.media.common.constant.MediaErrorCode;
import com.xuecheng.media.common.utils.AliyunVODUtil;
import com.xuecheng.media.convert.MediaConvert;

import com.xuecheng.media.entity.Media;
import com.xuecheng.media.mapper.MediaMapper;
import com.xuecheng.media.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 媒资信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Value("${aliyun.region}")
    private String region;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Autowired
    private ContentApiAgent contentApiAgent;

    @Override
    public MediaDTO createMedia(MediaDTO dto) {
        /*
         * 业务分析：
         *  1.判断关键数据
         *   数据库字段不能为空
          fileName、fileId（videoid）、companyId
           */
        if (ObjectUtils.isEmpty(dto.getFileId()) ||
                ObjectUtils.isEmpty(dto.getFilename()) ||
                ObjectUtils.isEmpty(dto.getCompanyId())) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2. 判断媒资信息是否存在
        //依据条件：fileId（videoid）
        //判断是否是同一家机构
        LambdaQueryWrapper<Media> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Media::getFileId, dto.getFileId());
        Media media = this.getOne(queryWrapper);
        if (!ObjectUtils.isEmpty(media)) {
            ExceptionCast.cast(MediaErrorCode.E_140008);
        }
        //3. 判断媒资的类型
        //前端会传入媒资类型
        //如果是视频信息，需要保存媒资信息的播放地址
        if (ResourceTypeEnum.VIDEO.getCode().equals(dto.getType())) {
            try {
                DefaultAcsClient client = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);
                GetPlayInfoResponse playInfo = AliyunVODUtil.getPlayInfo(client, dto.getFileId());
                List<GetPlayInfoResponse.PlayInfo> playInfoList = playInfo.getPlayInfoList();
                if (!CollectionUtils.isEmpty(playInfoList)) {
                    GetPlayInfoResponse.PlayInfo playInfo1 = playInfoList.get(0);
                    String playURL = playInfo1.getPlayURL();
                    dto.setUrl(playURL);
                }

            } catch (Exception e) {
                log.error("获取视频播放地址失败");
            }
        }

        // 4. 保存数据
        // dto - > po
        Media media1 = MediaConvert.INSTANCE.dto2entity(dto);

        boolean saveFlag = this.save(media1);
        // 5. 判断操作结果并返回结果数据
        ExceptionCast.cast(!saveFlag, MediaErrorCode.E_140001);
        return dto;
    }

    @Override
    public String getVODUrl(Long mediaId, Long companyId) {
        //判断关键数据
        if (ObjectUtils.isEmpty(mediaId) || ObjectUtils.isEmpty(companyId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //判断媒资信息
        Media media = this.getById(mediaId);
        if (ObjectUtils.isEmpty(media)) {
            ExceptionCast.cast(CommonErrorCode.E_100104);
        }
        //判断是否是同一家公司
        if (!(ObjectUtils.isEmpty(media.getCompanyId()))) {
            if (!companyId.equals(media.getCompanyId())) {
                ExceptionCast.cast(CommonErrorCode.E_100108);
            }
        }
        DefaultAcsClient client = null;
        String playURL = null;
        try {
            client = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);
            GetPlayInfoResponse playInfo = AliyunVODUtil.getPlayInfo(client, media.getFileId());
            List<GetPlayInfoResponse.PlayInfo> playInfoList = playInfo.getPlayInfoList();
            if (!CollectionUtils.isEmpty(playInfoList)) {
                GetPlayInfoResponse.PlayInfo playInfo1 = playInfoList.get(0);
                playURL = playInfo1.getPlayURL();
            }
        } catch (Exception e) {
            log.error("获得视频播放地址失败：{}", e.getMessage());
            ExceptionCast.cast(MediaErrorCode.E_140012);
        }
        // 4.返回媒资的资源地址
        return playURL;
    }

    @Override
    public PageVO<MediaDTO> queryMediaList(PageRequestParams params, QueryMediaModel model) {
        if (params.getPageNo() < 1) {
            params.setPageNo(PageRequestParams.DEFAULT_PAGE_NUM);
        }
        if (params.getPageSize() < 1) {
            params.setPageSize(PageRequestParams.DEFAULT_PAGE_SIZE);
        }
        LambdaQueryWrapper<Media> queryWrapper = new LambdaQueryWrapper<>();
        //名字模糊查询
        if (StringUtil.isNotBlank(model.getFilename())) {

            queryWrapper.like(Media::getFilename, model.getFilename());
        }

        if (StringUtil.isNotBlank(model.getType())) {
            // 类型查询eq
            queryWrapper.eq(Media::getType, model.getType());
        }
        //添加对媒资审核状态的条件
        if (StringUtils.isNotBlank(model.getAuditStatus())) {
            queryWrapper.eq(Media::getAuditStatus, model.getAuditStatus());
        }

        // 适配运营平台和教学机构条件
        Long companyId = model.getCompanyId();
        if (ObjectUtils.isEmpty(companyId)){
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        queryWrapper.eq(Media::getCompanyId, companyId);
        //未删除
        queryWrapper.eq(Media::getStatus,"1");
        //创建分页数据
        Page<Media> page = new Page<>(params.getPageNo(), params.getPageSize());
        //根据分页和查询调价查询list数据
        Page<Media> pageResult = this.page(page, queryWrapper);

        long total = pageResult.getTotal();
        List<Media> records = pageResult.getRecords();

        // 代码
        List<MediaDTO> dtos = Collections.EMPTY_LIST;
        // 使用mapstruct转换器将pos转为dtos
        if (!CollectionUtils.isEmpty(records)) {
            dtos = MediaConvert.INSTANCE.entity2dtos(records);
        }

        PageVO pageVO = new PageVO(dtos, total, params.getPageNo(), params.getPageSize());

        return pageVO;
    }

    @Override
    public boolean deleteMedia(Long mediaId, Long companyId) {
        //1。判断关键信息
        if (ObjectUtils.isEmpty(mediaId) || ObjectUtils.isEmpty(companyId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        //判断媒资信息是否存在
        Media media = this.getById(mediaId);
        if (ObjectUtils.isEmpty(media)) {
            ExceptionCast.cast(CommonErrorCode.E_100104);
        }
        //判断是否是同一家公司
        if (!(ObjectUtils.isEmpty(media.getCompanyId()))) {
            if (!companyId.equals(media.getCompanyId())) {
                ExceptionCast.cast(CommonErrorCode.E_100108);
            }
        }
        //判断媒资信息是否已删除
        if (CommonEnum.DELETE_FLAG.getCodeInt().equals(media.getStatus())) {
            ExceptionCast.cast(MediaErrorCode.E_140016);
        }
        //判断是否通过审核
        ExceptionCast.cast(AuditEnum.AUDIT_PASTED_STATUS.getCode().equals(media.getAuditStatus().toString()), MediaErrorCode.E_140009);
        //判断是否绑定课程
        RestResponse<Boolean> response = contentApiAgent.verifyTreeNodeByCourseId(mediaId);
        Boolean result1 = response.getResult();
        if (result1) {
            ExceptionCast.cast(MediaErrorCode.E_140003);
        }
        // 修改媒资信息状态值
        LambdaUpdateWrapper<Media> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Media::getStatus, CommonEnum.DELETE_FLAG.getCodeInt());
        updateWrapper.set(Media::getChangeDate, LocalDateTime.now());
        updateWrapper.eq(Media::getId, media.getId());
        boolean result = this.update(updateWrapper);
        if (!result) {
            ExceptionCast.cast(MediaErrorCode.E_140002);
        }
        //结果返回
        return result;
    }

    @Override
    public RestResponse<MediaDTO> getById4Service(Long mediaId) {

        // 1.判断关键数据
        if (ObjectUtils.isEmpty(mediaId)) {
            return RestResponse.validfail(CommonErrorCode.E_100101);
        }

        // 2.判断业务数据
        Media media = this.getById(mediaId);

        if (ObjectUtils.isEmpty(media)) {
            return RestResponse.validfail(MediaErrorCode.E_140005);
        } else {

            MediaDTO mediaDTO = MediaConvert.INSTANCE.entity2dto(media);
            return RestResponse.success(mediaDTO);
        }
    }

    @Override
    public PageVO<MediaDTO> mQueryMediaList(PageRequestParams params, QueryMediaModel model) {
        if (params.getPageNo() < 1) {
            params.setPageNo(PageRequestParams.DEFAULT_PAGE_NUM);
        }
        if (params.getPageSize() < 1) {
            params.setPageSize(PageRequestParams.DEFAULT_PAGE_SIZE);
        }
        LambdaQueryWrapper<Media> queryWrapper = new LambdaQueryWrapper<>();
        //名字模糊查询
        if (StringUtil.isNotBlank(model.getFilename())) {

            queryWrapper.like(Media::getFilename, model.getFilename());
        }

        if (StringUtil.isNotBlank(model.getType())) {
            // 类型查询eq
            queryWrapper.eq(Media::getType, model.getType());
        }
        //添加对媒资审核状态的条件
        if (StringUtils.isNotBlank(model.getAuditStatus())) {
            queryWrapper.eq(Media::getAuditStatus, model.getAuditStatus());
        }

        //未删除
        queryWrapper.eq(Media::getStatus,"1");
        //创建分页数据
        Page<Media> page = new Page<>(params.getPageNo(), params.getPageSize());
        //根据分页和查询调价查询list数据
        Page<Media> pageResult = this.page(page, queryWrapper);

        long total = pageResult.getTotal();
        List<Media> records = pageResult.getRecords();

        // 代码
        List<MediaDTO> dtos = Collections.EMPTY_LIST;
        // 使用mapstruct转换器将pos转为dtos
        if (!CollectionUtils.isEmpty(records)) {
            dtos = MediaConvert.INSTANCE.entity2dtos(records);
        }

        PageVO pageVO = new PageVO(dtos, total, params.getPageNo(), params.getPageSize());

        return pageVO;
    }

    @Override
    public String mGetVODUrl(Long mediaId, Long companyId) {
        //判断关键数据
        if (ObjectUtils.isEmpty(mediaId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //判断媒资信息
        Media media = this.getById(mediaId);
        if (ObjectUtils.isEmpty(media)) {
            ExceptionCast.cast(CommonErrorCode.E_100104);
        }
        DefaultAcsClient client = null;
        String playURL = null;
        try {
            client = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);
            GetPlayInfoResponse playInfo = AliyunVODUtil.getPlayInfo(client, media.getFileId());
            List<GetPlayInfoResponse.PlayInfo> playInfoList = playInfo.getPlayInfoList();
            if (!CollectionUtils.isEmpty(playInfoList)) {
                GetPlayInfoResponse.PlayInfo playInfo1 = playInfoList.get(0);
                playURL = playInfo1.getPlayURL();
            }
        } catch (Exception e) {
            log.error("获得视频播放地址失败：{}", e.getMessage());
            ExceptionCast.cast(MediaErrorCode.E_140012);
        }
        // 4.返回媒资的资源地址
        return playURL;
    }

    @Override
    @Transactional
    public void auditMedia(MediaAuditDTO auditDTO) {
        // 1.判断关键数据
        // id           媒资id
        // mediaAuditStatus  媒资审核状态
        // mediaAutditMind   媒资审核意见
        String auditStatus = auditDTO.getAuditStatus();
        if (ObjectUtils.isEmpty(auditDTO.getId()) ||
                StringUtil.isBlank(auditStatus) ||
                StringUtil.isBlank(auditDTO.getAuditMind())
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //  2.判断业务数据
        Media media = this.getById(auditDTO.getId());
        // 判断媒资是否存在
        ExceptionCast.cast(ObjectUtils.isEmpty(media),
                MediaErrorCode.E_140005);
        // 判断媒资是否删除
        ExceptionCast.cast(CommonEnum.DELETE_FLAG.getCodeInt().equals(media.getStatus()),
                MediaErrorCode.E_140016);

        // 判断媒资审核状态：只能是未审核
        ExceptionCast.cast(!(AuditEnum.AUDIT_UNPAST_STATUS.getCode().equals(media.getAuditStatus())),
                MediaErrorCode.E_140018);

        // 3.修改媒资审核的数据
        //    修改CourseBase表
        //        audtiStatus状态
        //        auditMind审核意见
        //        changeDate审核时间
        LambdaUpdateWrapper<Media> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Media::getAuditStatus, auditStatus);
        updateWrapper.set(Media::getAuditMind, auditDTO.getAuditMind());
        updateWrapper.set(Media::getChangeDate, LocalDateTime.now());
        updateWrapper.eq(Media::getId, auditDTO.getId());

        // 4.判断修改的结果
        boolean result = this.update(updateWrapper);
        ExceptionCast.cast(!result,MediaErrorCode.E_140017);

    }
}

