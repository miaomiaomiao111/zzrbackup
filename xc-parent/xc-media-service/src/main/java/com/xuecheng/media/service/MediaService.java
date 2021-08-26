package com.xuecheng.media.service;

import com.xuecheng.api.media.model.dto.MediaAuditDTO;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.api.media.model.vo.MediaAuditVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.media.entity.Media;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 媒资信息 服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-10
 */
public interface MediaService extends IService<Media> {
    /**
     * 保存媒资信息
     * @param dto
     * @return
     */
    MediaDTO createMedia(MediaDTO dto);

    /**
     * 机构或平台预览课程内容
     * @param mediaId
     * @return
     */
    String getVODUrl(Long mediaId,Long companyId);

    /**
     * 查询媒资信息功能
     * @param params
     * @param model
     * @param companyId
     * @return
     */
    PageVO<MediaDTO> queryMediaList(PageRequestParams params, QueryMediaModel model);

    /**
     * 删除媒资功能
     * @param mediaId
     * @param companyId
     * @return
     */
    boolean deleteMedia(Long mediaId, Long companyId);

    /**
     * 根据id查询媒资信息-远端服务调用
     * @param mediaId
     * @return
     */
    RestResponse<MediaDTO> getById4Service(Long mediaId);

    /**
     * 运营平台查询媒资信息
     * @param params
     * @param model
     * @return
     */
    PageVO<MediaDTO> mQueryMediaList(PageRequestParams params, QueryMediaModel model);

    String mGetVODUrl(Long mediaId, Long companyId);

    void auditMedia(MediaAuditDTO mediaDTO);
}
