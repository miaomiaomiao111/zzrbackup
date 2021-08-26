package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.content.entity.TeachplanMedia;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-07
 */
public interface TeachplanMediaService extends IService<TeachplanMedia> {

    RestResponse<Boolean> queryTeachplanMediaByMediaId(Long mediaId);
}
