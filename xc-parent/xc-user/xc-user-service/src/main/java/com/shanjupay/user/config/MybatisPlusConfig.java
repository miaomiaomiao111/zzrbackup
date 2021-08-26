package com.shanjupay.user.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <P>
 * Mybatis-Plus 配置
 * </p>
 */
@Configuration
@MapperScan("com.shanjupay.**.mapper")
public class MybatisPlusConfig {

	/**
	 * 分页插件，自动识别数据库类型
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

}