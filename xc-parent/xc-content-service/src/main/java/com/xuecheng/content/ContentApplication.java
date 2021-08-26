package com.xuecheng.content;


import com.spring4all.swagger.EnableSwagger2Doc;
import com.xuecheng.agent.content.TeachingApiAgent;
import com.xuecheng.api.media.agent.MediaApiAgent;
import com.xuecheng.api.system.agent.SystemApiAgent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@RefreshScope            //开启动态刷新
@EnableDiscoveryClient   //开启注册中心
@EnableSwagger2Doc       //开启swagger
//scanBasePackages扫面包，异常处理
@EnableFeignClients(basePackageClasses = {MediaApiAgent.class, SystemApiAgent.class, TeachingApiAgent.class})
@SpringBootApplication(scanBasePackages = {"com.xuecheng.common.exception", "com.xuecheng.content"})
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}