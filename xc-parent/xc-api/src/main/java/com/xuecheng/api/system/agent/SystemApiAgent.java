package com.xuecheng.api.system.agent;

import com.xuecheng.api.system.agent.fallback.SystemApiAgentFallBack;
import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import com.xuecheng.common.domain.response.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 *     内容管理对系统管理的 Feign 的远程调用
 * </p>
 *
 * @Description:
 */
@FeignClient(value = XcFeignServiceNameList.XC_SYSTEM_SERVICE,fallback = SystemApiAgentFallBack.class)
public interface SystemApiAgent {

    @GetMapping("/system/l/course-category/{id}")
    RestResponse<CourseCategoryDTO> getById(@PathVariable String id);

}