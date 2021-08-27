package com.xuecheng.agent.teaching;

import com.xuecheng.agent.teaching.sentinel.CommentApiAgentFallBack;
import com.xuecheng.api.content.model.dto.CoursePub;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 教学服务 远程调用内容接口
 * @author zzr
 */
@FeignClient(value = XcFeignServiceNameList.XC_CONTENT_SERVICE)
public interface ContentApiAgent {
    @GetMapping("content/l/coursePub/{targetId}")
    CoursePub getById(@PathVariable(value = "targetId") Long targetId);

}
