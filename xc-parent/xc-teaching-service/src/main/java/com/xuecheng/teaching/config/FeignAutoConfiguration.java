package com.xuecheng.teaching.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p></p>
 * feign 和 sentinel 自动配置类
 * @Description:
 */
@Configuration
@ComponentScan("com.xuecheng.agent.teaching.sentinel")
@EnableFeignClients(basePackages = "com.xuecheng.agent")
public class FeignAutoConfiguration {
}