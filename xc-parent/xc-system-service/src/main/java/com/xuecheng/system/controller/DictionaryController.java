package com.xuecheng.system.controller;

import com.xuecheng.api.system.DictionaryApi;
import com.xuecheng.api.system.model.dto.DictionaryDTO;
import com.xuecheng.system.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class DictionaryController implements DictionaryApi {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping(value = "dictionary/all",name = "查询全部数据字典信息(常量信息-便于前端数据查询后的持久化操作")
    public List<DictionaryDTO> queryAll() {
        return dictionaryService.queryAll();
    }


    @GetMapping(value = "dictionary/{code}", name = "根据 code 查询数据字典信息")
    public DictionaryDTO getDictionaryByCode(@PathVariable String code) {
        DictionaryDTO dictionaryDTO = dictionaryService.getDictionaryByCode(code);
        return dictionaryDTO;
    }


}
