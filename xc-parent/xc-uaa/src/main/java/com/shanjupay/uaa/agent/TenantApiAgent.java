package com.shanjupay.uaa.agent;

import com.shanjupay.user.api.dto.resource.ApplicationDTO;
import com.shanjupay.user.api.dto.tenant.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 创建用户（来自UAA）
 */
@FeignClient(value = "user-service")
public interface TenantApiAgent {

    @PostMapping("/user/login")
    public LoginInfoDTO login(@RequestBody LoginRequestDTO loginRequest);

    @GetMapping("/user/apps/{clientId}")
    public ApplicationDTO getApplicationDTOByClientId(@PathVariable("clientId") String clientId);

}

