package com.xuecheng.media.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.xuecheng.api.media.MediaApi;
import com.xuecheng.api.media.aliyun.VodUploadRequest;
import com.xuecheng.api.media.aliyun.VodUploadToken;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.api.media.model.vo.MediaVO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.media.common.constant.MediaErrorCode;
import com.xuecheng.media.common.utils.AliyunVODUtil;
import com.xuecheng.media.common.utils.UAASecurityUtil;
import com.xuecheng.media.convert.MediaConvert;
import com.xuecheng.media.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class MediaController implements MediaApi {

    @Autowired
    private MediaService mediaService;


    @Value("${aliyun.region}")
    private String region;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @PostMapping("media/vod-token")
    public VodUploadToken generateVodToken(@RequestBody VodUploadRequest request) {
        // 判断关键数据
        String fileName = request.getFileName();
        String title = request.getTitle();
        if (StringUtil.isBlank(fileName) ||
                StringUtil.isBlank(title)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }


        VodUploadToken uploadToken = null;
        try {
            //初始化客户端对象
            DefaultAcsClient acsClient = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);
            //获取上传的凭证
            CreateUploadVideoResponse response = AliyunVODUtil.createUploadVideo(acsClient, title, fileName);
            //封装响应结果数据
            uploadToken = new VodUploadToken();

            String requestId = response.getRequestId();
            String videoId = response.getVideoId();
            String uploadAddress = response.getUploadAddress();
            String uploadAuth = response.getUploadAuth();

            uploadToken.setUploadAuth(uploadAuth);
            uploadToken.setUploadAddress(uploadAddress);
            uploadToken.setVideoId(videoId);
            uploadToken.setRequestId(requestId);
        } catch (Exception e) {
            // 系统异常 --> 业务异常
            log.error(MediaErrorCode.E_140011.getDesc() + ": {}", e.getMessage());
            ExceptionCast.cast(MediaErrorCode.E_140011);
            // e.printStackTrace();
        }
        return uploadToken;
    }


    @GetMapping("media/refresh-vod-token/{videoId}")
    public VodUploadToken refreshVodToken(@PathVariable String videoId) {

        //判断关键数据
        ExceptionCast.cast(StringUtil.isBlank(videoId), CommonErrorCode.E_100101);
        VodUploadToken uploadToken = null;


        try {
            //初始化客户端对象
            DefaultAcsClient client = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);


            RefreshUploadVideoResponse response = AliyunVODUtil.refreshUploadVideo(client, videoId);


            // 获得结果数据-->刷新后的凭证
            String requestId = response.getRequestId();
            String uploadAddress = response.getUploadAddress();
            String uploadAuth = response.getUploadAuth();

            uploadToken = new VodUploadToken();

            uploadToken.setUploadAuth(uploadAuth);
            uploadToken.setUploadAddress(uploadAddress);
            uploadToken.setVideoId(videoId);
            uploadToken.setRequestId(requestId);
        } catch (Exception e) {

            log.error(MediaErrorCode.E_140015.getDesc() + ": {}", e.getMessage());
            ExceptionCast.castWithExceptionMsg(MediaErrorCode.E_140015, e.getMessage());

        }


        return uploadToken;
    }

    @PostMapping(value = "media/list",name = "机构查询媒资列表")
    public PageVO queryCourseList(PageRequestParams params,
                                  @RequestBody QueryMediaModel model) {

        //1.获得访问令牌并从中解析出机构的信息Id数据
        long companyId = UAASecurityUtil.getCompanyId();

        model.setCompanyId(companyId);
        //2.调用service层的方法
        PageVO<MediaDTO> pageVo = mediaService.queryMediaList(params, model);

        return pageVo;
    }

    @PostMapping(value = "media",name = "上传媒资")
    public MediaDTO createMedia(@RequestBody MediaVO vo) {
        //1.获取机构ID
        Long companyId = UAASecurityUtil.getCompanyId();
        MediaDTO mediaDTO = MediaConvert.INSTANCE.vo2dto(vo);
        mediaDTO.setCompanyId(companyId);
        MediaDTO mediaDTO1 = mediaService.createMedia(mediaDTO);
        return mediaDTO1;
    }


    @GetMapping("/media/preview/{mediaId}")
    public String previewMedia(@PathVariable Long mediaId) {

        Long companyId = UAASecurityUtil.getCompanyId();

        String vodUrl = mediaService.getVODUrl(mediaId, companyId);

        return vodUrl;
    }

    @GetMapping("m/media/preview/{mediaId}")
    public String mPreviewMedia(@PathVariable Long mediaId) {

        Long companyId = UAASecurityUtil.getCompanyId();

        String vodUrl = mediaService.mGetVODUrl(mediaId, companyId);

        return vodUrl;
    }

    @DeleteMapping(value = "media/{mediaId}", name = "删除媒资信息")
    public boolean deleteMedia(@PathVariable("mediaId") Long mediaId) {

        Long companyId = UAASecurityUtil.getCompanyId();
        return mediaService.deleteMedia(mediaId, companyId);
    }

    @GetMapping("l/media/{mediaId}")
    public RestResponse<MediaDTO> getMediaById(@PathVariable Long mediaId) {

        RestResponse<MediaDTO> response = mediaService.getById4Service(mediaId);

        return response;
    }

    @PostMapping(value = "m/media/list",name = "运营查询媒资列表")
    public PageVO queryCourseList4M(PageRequestParams params,
                                  @RequestBody QueryMediaModel model) {

        //2.调用service层的方法
        PageVO<MediaDTO> pageVo = mediaService.mQueryMediaList(params, model);

        return pageVo;
    }


}
