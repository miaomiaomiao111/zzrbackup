package com.xuecheng.media.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充创建时间和更新时间
 */
@Slf4j
@Component
public class DateTimeMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("start insert fill ....");
		this.setFieldValByName("createDate", LocalDateTime.now(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("start update fill ....");
		this.setFieldValByName("finishDate", LocalDateTime.now(), metaObject);
		this.setFieldValByName("modifyDate", LocalDateTime.now(), metaObject);
		this.setFieldValByName("changeDate", LocalDateTime.now(), metaObject);
	}
}
