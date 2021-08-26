package com.xuecheng.search;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {
    @Value("${xuecheng.elasticsearch.course.index}")
    private String indexName;

    @Autowired
    private RestHighLevelClient client;
    @Test
    public void testDeleteDocument() throws IOException {
        // 1.准备Request
        DeleteRequest request = new DeleteRequest(indexName, "29");
        // 2.发送请求
        client.delete(request, RequestOptions.DEFAULT);
    }

}
