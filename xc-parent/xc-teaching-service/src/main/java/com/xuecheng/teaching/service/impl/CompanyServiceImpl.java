package com.xuecheng.teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.teaching.convert.CompanyConvert;
import com.xuecheng.teaching.entity.Company;
import com.xuecheng.teaching.mapper.CompanyMapper;
import com.xuecheng.teaching.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService{

    public CompanyDTO getByTenantId(Long tenantId) {
        if (tenantId == null || tenantId <= 0) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Company::getTenantId, tenantId);
        Company company = this.getOne(queryWrapper);
        CompanyDTO companyDTO = CompanyConvert.INSTANCE.entity2dto(company);
        return companyDTO;
    }
}
