package com.xuecheng.api.teaching.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel(value="CompanyDTO", description="教育机构")
public class CompanyDTO implements Serializable {

    public static final String ORG_TYPE_COMPANY = "c";
    public static final String ORG_TYPE_PERSONAL = "p";

    @ApiModelProperty(value = "公司Id值", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long companyId;

    /*@ApiModelProperty(value = "用户名(机构账号)", required = true)
    private String username;*/

    @ApiModelProperty(value = "名称", required = true)
    @NotEmpty(message = "公司名称不能为空")
    private String name;

    @ApiModelProperty(value = "机构logo")
    private String logo;

    @ApiModelProperty(value = "联系方式", required = true)
    @NotEmpty(message = "机构联系方式不能为空")
    @Pattern(regexp = "\\d{11,12}", message = "机构联系方式格式有误")
    private String phone;


    @ApiModelProperty(value = "手机验证标识符", required = true)
    @NotEmpty(message = "手机验证标识不能为空")
    @Min(value = 4,  message = "手机验证标识格式有误")
    private String verifyKey;

    @ApiModelProperty(value = "手机验证码", required = true)
    @NotEmpty(message = "手机验证码不能为空")
    @Min(value = 4,  message = "手机验证码格式有误")
    private String verifyCode;



    @ApiModelProperty(value = "申请人名称")
    @NotEmpty(message = "申请人不能为空")
    private String linkname;

    @ApiModelProperty(value = "手持身份证照片")
    private String handPhotos;

    @ApiModelProperty(value = "机构地址", required = true)
    @NotEmpty(message = "机构地址不能为空")
    @Size(max = 120, message = "机构地址最长120个字符")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "承诺书")
    private String promiseLetter;



    @ApiModelProperty(value = "商标注册证")
    private String trademarkPic;

    @ApiModelProperty(value = "机构简介")
    private String intro;

    @ApiModelProperty(value = "一句话简介")
    private String briefIntro;

    private String mainCategory;


    @ApiModelProperty(value = "身份证照片")
    private String identityPic;


    @ApiModelProperty(value = "营业执照")
    private String businessPic;

    @ApiModelProperty(value = "营业执照有效期")
    private String businessTerm;

    @ApiModelProperty(value = "营业执照注册号")
    private String businessRegistNo;

    @ApiModelProperty(value = "教育资质证照片")
    private String eduCertImg;

    @ApiModelProperty(value = "教育资质证书有效期")
    private String eduCertTerm;

    @ApiModelProperty(value = "教育资质证书注册号")
    private String eduCertNo;


    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "审批状态")
    private String approvalStatus;

    @ApiModelProperty(value = "审批意见")
    private String approvalComment;

    @ApiModelProperty(value = "审批日期")
    private LocalDateTime approvalDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    private LocalDateTime createDate;

    private LocalDateTime changeDate;


    @ApiModelProperty(value = "机构名称", required = true)
    private String title;


    @ApiModelProperty(value = "tId", hidden = true)
    private Long tenantId;
}
