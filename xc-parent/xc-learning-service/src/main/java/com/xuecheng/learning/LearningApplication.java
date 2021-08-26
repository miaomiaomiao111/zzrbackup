package com.xuecheng.learning;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2Doc
@ComponentScan({"com.xuecheng.learning", "com.xuecheng.common.exception"})
@EnableFeignClients(basePackages = {"com.xuecheng.learning.agent","com.xuecheng.agent.order"})
public class LearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningApplication.class, args);
	}
}