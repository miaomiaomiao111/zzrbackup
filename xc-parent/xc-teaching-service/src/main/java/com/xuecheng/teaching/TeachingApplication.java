package com.xuecheng.teaching;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2Doc
@ComponentScan({"com.xuecheng.teaching", "com.xuecheng.common.exception"})
public class TeachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeachingApplication.class, args);
	}
}