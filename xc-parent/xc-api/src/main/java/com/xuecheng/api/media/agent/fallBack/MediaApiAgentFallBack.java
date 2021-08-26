package com.xuecheng.api.media.agent.fallBack;

import com.xuecheng.api.media.agent.MediaApiAgent;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import org.springframework.stereotype.Component;


/**
 * <p>
 *     内容管理对媒资的 Feign 的远程调用
 * </p>
 *
 * @Description:
 */
@Component
public class MediaApiAgentFallBack implements MediaApiAgent {


    @Override
    public RestResponse getById(Long mediaId) {

        RestResponse<Object> validfail = RestResponse.validfail(CommonErrorCode.E_999981);

        return validfail;
    }
}