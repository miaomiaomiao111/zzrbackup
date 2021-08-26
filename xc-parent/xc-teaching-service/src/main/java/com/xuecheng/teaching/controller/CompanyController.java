package com.xuecheng.teaching.controller;

import com.xuecheng.api.teaching.CompanyApi;
import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.teaching.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 教育机构 前端控制器
 */
@Slf4j
@RestController
public class CompanyController implements CompanyApi {

    @Autowired
    private CompanyService companyService;

    @GetMapping("l/company/{tenantId}")
    public CompanyDTO getByTenantId(@PathVariable("tenantId") Long tenantId) {
        return companyService.getByTenantId(tenantId);
    }

}
