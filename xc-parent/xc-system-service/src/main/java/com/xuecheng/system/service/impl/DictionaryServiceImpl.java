package com.xuecheng.system.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.system.model.dto.DictionaryDTO;
import com.xuecheng.system.convert.DictionaryConvert;
import com.xuecheng.system.entity.Dictionary;
import com.xuecheng.system.mapper.DictionaryMapper;
import com.xuecheng.system.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {
    @Override
    public DictionaryDTO getDictionaryByCode(String code) {

        //1.判断数据的合法性
        if (StringUtils.isBlank(code)) {
            return null;
        }
        //2.根据Code进行查询
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getCode, code);
        Dictionary dictionary = getOne(queryWrapper);

        //3.将 PO 转为 DTO
        DictionaryDTO dictionaryDTO = null;
        if (dictionary != null) {
            dictionaryDTO = DictionaryConvert.INSTANCE.entity2dto(dictionary);
        } else {
            dictionaryDTO = new DictionaryDTO();
        }

        //4.返回结果
        return dictionaryDTO;
    }

    @Override
    public List<DictionaryDTO> queryAll() {
        //1.查询全部数据
        List<Dictionary> dictionaryList = this.list();

        //2.将POs转为DTOs并返回
        List<DictionaryDTO> dtos = DictionaryConvert.INSTANCE.entitys2dtos(dictionaryList);
        return dtos;
    }
}
