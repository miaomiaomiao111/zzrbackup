package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.entity.TeachplanMedia;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.service.TeachplanMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class TeachplanMediaServiceImpl extends ServiceImpl<TeachplanMediaMapper, TeachplanMedia> implements TeachplanMediaService {

    @Override
    public RestResponse<Boolean> queryTeachplanMediaByMediaId(Long mediaId) {
        //判断关键数据
        if (ObjectUtils.isEmpty(mediaId)){
            return RestResponse.validfail(CommonErrorCode.E_100101);
        }
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getMediaId,mediaId);
        TeachplanMedia teachplanMedia = getOne(queryWrapper);
        if ((ObjectUtils.isEmpty(teachplanMedia))){
            return RestResponse.success(false);
        }
        return RestResponse.success(true);
    }
}
