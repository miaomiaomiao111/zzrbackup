package com.xuecheng.api.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author itcast
 */
@Data
@ApiModel(value="DictionaryDTO", description="数据字典")
public class DictionaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id标识")
    private Long DicId;

    @ApiModelProperty(value = "数据字典名称")
    private String name;

    @ApiModelProperty(value = "数据字典代码")
    private String code;

    @ApiModelProperty(value = "数据字典项--json格式 ")
    private String itemValues;


}
