package com.xuecheng.teaching.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 教育机构
 * </p>
 */
@Data
@TableName("company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * c机构 p个人
     * 机构类别
     */
    private String orgType;


    private String logo;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 申请人名称
     */
    private String linkname;

    private String handPhotos;

    /**
     * 机构地址
     */
    private String address;

    private String email;

    private String promiseLetter;

    private String trademarkPic;
    /**
     * 简介
     */
    private String intro;


    private String briefIntro;

    private String mainCategory;

    /**
     * 身份证照片
     */
    private String identityPic;

    /**
     * 营业执照
     */
    private String businessPic;

    private String businessTerm;

    private String businessRegistNo;

    private String eduCertImg;

    private String eduCertTerm;

    private String eduCertNo;

    /**
     * 状态
     * @See com.xuecheng.common.enums.common.CommonEnum
     */
    private Integer status;

    /**
     * @See com.xuecheng.common.enums.common.AuditEnum
     */
    private String approvalStatus;
    /**
     * 审批意见
     */
    private String approvalComment;

    private String approvalPeople;

    private LocalDateTime approvalDate;

    private Integer approvalNums;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;

    /**
     * 机构名称
     */
    private String title;



    private Long tenantId;
}
