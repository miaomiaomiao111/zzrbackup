package com.xuecheng.api.content.agent;



import com.xuecheng.common.constant.XcFeignServiceNameList;
import com.xuecheng.common.domain.response.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = XcFeignServiceNameList.XC_CONTENT_SERVICE)
public interface ContentApiAgent {

    @GetMapping("content/teachplanMedia/{mediaId}")
    RestResponse<Boolean> verifyTreeNodeByCourseId(@PathVariable("mediaId") Long mediaId);
}