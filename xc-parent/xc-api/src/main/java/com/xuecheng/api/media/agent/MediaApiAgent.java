package com.xuecheng.api.media.agent;

import com.xuecheng.api.media.agent.fallBack.MediaApiAgentFallBack;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import com.xuecheng.common.domain.response.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = XcFeignServiceNameList.XC_MEDIA_SERVICE,fallback = MediaApiAgentFallBack.class)
public interface MediaApiAgent {

    @GetMapping("/media/l/media/{mediaId}")
    RestResponse<MediaDTO> getById(@PathVariable Long mediaId);
}