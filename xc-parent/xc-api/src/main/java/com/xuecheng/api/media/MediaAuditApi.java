package com.xuecheng.api.media;

import com.xuecheng.api.media.model.vo.MediaAuditVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

@Api("媒资审核服务Api")
public interface MediaAuditApi {

    @ApiOperation("审核媒资")
    @ApiImplicitParam(name = "auditVO",value = "审核信息Vo",required = true,paramType = "body",dataType = "MediaAuditVO")
    void auditMedia(MediaAuditVO auditVO);
}
