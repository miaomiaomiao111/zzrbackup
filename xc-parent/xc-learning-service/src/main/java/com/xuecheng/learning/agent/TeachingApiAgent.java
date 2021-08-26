package com.xuecheng.learning.agent;

import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *  Feign远程调用 教学机构管理
 */
@FeignClient(value = XcFeignServiceNameList.XC_TEACHING_SERVICE)
public interface TeachingApiAgent {

    String SERVICE_CONTEXT_PRE = "/teaching";

    /**
     * 根据租户Id获取机构信息（机构时一种租户）
     * @param tenantId
     * @return
     */
    @GetMapping(SERVICE_CONTEXT_PRE + "/l/company/{tenantId}")
    CompanyDTO getCompInfoDetail(@PathVariable(value = "tenantId") Long tenantId);

}
