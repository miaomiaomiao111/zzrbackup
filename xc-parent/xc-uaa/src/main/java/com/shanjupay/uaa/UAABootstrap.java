package com.shanjupay.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.shanjupay.uaa.agent"})
@SpringBootApplication(scanBasePackages = {"com.shanjupay.uaa","com.xuecheng.common.exception"})
public class UAABootstrap {

	
	public static void main(String[] args) {
		SpringApplication.run(UAABootstrap.class, args);
	}
	

}
