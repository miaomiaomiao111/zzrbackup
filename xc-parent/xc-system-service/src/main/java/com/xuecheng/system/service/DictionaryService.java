package com.xuecheng.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.system.model.dto.DictionaryDTO;
import com.xuecheng.system.entity.Dictionary;


import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author itcast
 * @since 2021-08-06
 */
public interface DictionaryService extends IService<Dictionary> {

    DictionaryDTO getDictionaryByCode(String code);

    List<DictionaryDTO> queryAll();
}
