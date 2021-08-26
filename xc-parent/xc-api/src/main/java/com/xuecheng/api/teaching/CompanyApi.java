package com.xuecheng.api.teaching;

import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import io.swagger.annotations.ApiOperation;

/**
 * <p></p>
 *
 * @Description:
 */
public interface CompanyApi {

    @ApiOperation("根据租户的id来获得公司的数据")
    CompanyDTO getByTenantId(Long tenantId);

}
