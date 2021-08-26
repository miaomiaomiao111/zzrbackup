package com.xuecheng.content.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testPostMethod() {

        // 1.url路径地址
        String url = "http://localhost:56082/farming/generatetoken?origin=qiniu";
        // 2.构建请求体的json数据
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tokenType", "1");
        dataMap.put("scope", "http://qxi0efasf.hd-bkt.clouddn.com");
        dataMap.put("deadline", 3600);
        dataMap.put("key", "1.jpg");
        /**
         * 方法 postForEntity
         *  作用：发送post请求，并已制定的格式返回响应数据
         *  参数：
         *      1.url路径地址
         *      2.post请求器中的参数，再次会使用map类型来封装
         *      3.响应使用指定类型返回
         *      4.url路径的参数
         */
        // 3.发送请求
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, dataMap, Map.class);
        Map body = responseEntity.getBody();
        System.out.println(body);


    }


}
