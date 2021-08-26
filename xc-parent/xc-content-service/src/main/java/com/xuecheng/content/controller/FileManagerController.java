package com.xuecheng.content.controller;

import com.xuecheng.api.content.FileManagerApi;
import com.xuecheng.api.content.model.qn.UploadTokenResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p></p>
 *
 * @Description:
 */
@Slf4j
@RestController
public class FileManagerController implements FileManagerApi {
    @Autowired
    private RestTemplate restTemplate;
    /**
     * #文件系统微服的请求地址
     * file.service.url = http://127.0.0.1:56082/farming/generatetoken?origin=qiniu
     * #文件存储空间名称
     * file.service.bucket = yyzxuecheng
     * <p>
     * #文件存储区域的地址
     * file.service.upload.region = http://upload.qiniu.com
     * #文件访问的cdn加速域名
     *
     * @return
     */

    //微服务的请求地址
    @Value("${file.service.url}")
    private String url;
    //存储空间名称
    @Value("${file.service.bucket}")
    private String bucketName;
    //存储区域地址
    @Value("${file.service.upload.region}")
    private String region;
    //加速域名
    @Value("${cdn.domain}")
    private String domain;

    @GetMapping("common/qnUploadToken")
    public UploadTokenResult qiniuUploadToken() {
        // 1.访问文件系统微服务接口地址(已经在nacos中配置，无需定义)
        // 2.构建参数 v b
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tokenType", "1");
        dataMap.put("scope", bucketName);
        dataMap.put("deadline", 3600);
        // 保证文件key唯一
        String fileKey = UUID.randomUUID().toString() + RandomStringUtils.randomAlphanumeric(32);
        dataMap.put("key", fileKey);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, dataMap, Map.class);
        Map body = responseEntity.getBody();
        //设置uploadTokenResult
        UploadTokenResult uploadTokenResult = new UploadTokenResult();
        //设置令牌
        uploadTokenResult.setQnToken(body.get("result").toString());
        //设置token类型 1：获取上传凭证 2：下载 3：管理
        uploadTokenResult.setTokenType(dataMap.get("tokenType").toString());
        //设置存活时间
        uploadTokenResult.setDeadline(new Integer(dataMap.get("deadline").toString()));
        //设置cnd加速域名
        uploadTokenResult.setDomain(domain);
        //设置文件的唯一标识
        uploadTokenResult.setKey(fileKey);
        //设置存储区域地址
        uploadTokenResult.setUp_region(region);
        //设置存储空间的名称
        uploadTokenResult.setScope(bucketName);
        //返回
        return uploadTokenResult;
    }
}