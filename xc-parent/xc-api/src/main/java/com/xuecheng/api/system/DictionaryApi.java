package com.xuecheng.api.system;


import com.xuecheng.api.system.model.dto.DictionaryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.List;


@Api(tags = "系统-数据字典表", description = "对数据字典进行业务操作")
public interface DictionaryApi {

    @ApiOperation("根据 code 查询数据字典信息")
    @ApiImplicitParam(name = "code", value = "数据字典code",
            required = true, dataType = "String", paramType = "path")
    DictionaryDTO getDictionaryByCode(String code);

    @ApiOperation("查询全部数据字典信息(常量信息-便于前端数据查询后的持久化操作)")
    List<DictionaryDTO> queryAll();

}