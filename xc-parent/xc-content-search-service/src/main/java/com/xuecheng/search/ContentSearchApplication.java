package com.xuecheng.search;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.xuecheng.search","com.xuecheng.common.exception"})
public class ContentSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentSearchApplication.class, args);
    }

}
