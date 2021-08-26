package com.xuecheng.teaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.teaching.entity.Company;

/**
 * 教育机构 服务类
 */
public interface CompanyService extends IService<Company> {

    /**
     * 根据租户ID获取机构信息
     * @param tenantId
     * @return
     */
    CompanyDTO getByTenantId(Long tenantId);

}
