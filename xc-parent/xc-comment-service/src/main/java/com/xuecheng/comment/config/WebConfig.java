package com.xuecheng.comment.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <P>
 * WebMvc Config
 * </p>
 *
 * @author zhupeiyuan
 * @since 2019-05-09
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
	}
}
