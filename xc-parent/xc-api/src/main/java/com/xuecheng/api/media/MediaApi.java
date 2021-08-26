package com.xuecheng.api.media;

import com.xuecheng.api.media.aliyun.VodUploadRequest;
import com.xuecheng.api.media.aliyun.VodUploadToken;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.vo.MediaVO;
import com.xuecheng.common.domain.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p></p>
 *
 * @Description:
 */
@Api("媒资管服务Api")
public interface MediaApi {

    @ApiOperation("获得媒资文件上传凭证")
    VodUploadToken generateVodToken(VodUploadRequest request);


    @ApiOperation("刷新媒资文件上传凭证")
    @ApiImplicitParam(name = "videoId",value = "媒资文件的id值",required = true,paramType = "path",dataType = "String")
    VodUploadToken refreshVodToken(String videoId);

    @ApiOperation("保存媒资信息")
    @ApiImplicitParam(name = "vo", value = "媒资保存信息", required = true, dataType = "MediaVO", paramType = "body")
    MediaDTO createMedia(MediaVO vo);


    @ApiOperation("根据Id查询媒资信息")
    @ApiImplicitParam(name = "mediaId",
            value = "媒资ID", required = true,
            dataType = "long", paramType = "path", example = "1")
    RestResponse getMediaById(Long mediaId);

}