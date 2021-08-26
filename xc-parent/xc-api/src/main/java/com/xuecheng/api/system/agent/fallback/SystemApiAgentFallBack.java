package com.xuecheng.api.system.agent.fallback;

import com.xuecheng.api.system.agent.SystemApiAgent;
import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @Description:
 */
@Component
public class SystemApiAgentFallBack implements SystemApiAgent {

    @Override
    public RestResponse<CourseCategoryDTO> getById(String id) {
        return RestResponse.validfail(CommonErrorCode.E_999981);
    }
}