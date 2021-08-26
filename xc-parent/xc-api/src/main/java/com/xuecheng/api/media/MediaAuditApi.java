package com.xuecheng.api.media;

import com.xuecheng.api.media.model.vo.MediaAuditVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("媒资审核服务Api")
public interface MediaAuditApi {

    @ApiOperation("审核媒资")
    @ApiImplicitParam(name = "auditVO",value = "审核信息Vo",required = true,paramType = "body",dataType = "MediaAuditVO")
    void auditMedia(MediaAuditVO auditVO);
}
