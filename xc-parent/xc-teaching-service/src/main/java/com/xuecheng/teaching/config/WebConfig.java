package com.xuecheng.teaching.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <P>
 * WebMvc Config
 * </p>
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	/*@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
	}*/
}
