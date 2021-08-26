package com.xuecheng.media.test;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import org.junit.Test;

import java.util.List;

public class AliyunVodTest {

    private String accessKeyId = "LTAI5tRK1BAS3HpcHsfkMhfB";
    private String accessKeySecret = "s3C21S65XPUhjGmF3SoYiX7cwLHlxa";
    private String regionId = "cn-shanghai";


    /* vod 客户端对象初始化  */
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret, String regionId) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }


    @Test
    public void testGetUploadFileToken() throws Exception {

        // 1.创建请求对象（CreateUploadVideoRequest--创建一个媒资文件上传地址和凭证请求对象）
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle("this is a sample");
        request.setFileName("filename.mp4");

        // 2.初始化并获得客户端对象
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret, regionId);

        // 3.获得响应对象
        CreateUploadVideoResponse response = client.getAcsResponse(request);

        // 4.获得上传地址和上传的凭证
        try {
            System.out.print("VideoId = " + response.getVideoId() + "\n");
            System.out.print("UploadAddress = " + response.getUploadAddress() + "\n");
            System.out.print("UploadAuth = " + response.getUploadAuth() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

    @Test
    public void testGetPlayUrl() throws Exception {

        // 1.创建请求对象（获得视频播放地址的请求对象）
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("71580438f4bd4423aba4f7a71ecca333");

        // 2.初始化并获得客户端对象
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret, regionId);

        // 3.获得响应对象
        GetPlayInfoResponse response = client.getAcsResponse(request);

        // 4.获得视频播放地址
        try {

            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");

    }


}
