package com.xuecheng.media;


import com.spring4all.swagger.EnableSwagger2Doc;
import com.xuecheng.api.content.agent.ContentApiAgent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@RefreshScope            //开启动态刷新
@EnableDiscoveryClient   //开启注册中心
@EnableSwagger2Doc       //开启swagger
@EnableFeignClients(basePackageClasses = ContentApiAgent.class)
@SpringBootApplication(scanBasePackages = {"com.xuecheng.media","com.xuecheng.common.exception"})
public class MediaService {

	public static void main(String[] args) {
		SpringApplication.run(MediaService.class, args);
	}
}