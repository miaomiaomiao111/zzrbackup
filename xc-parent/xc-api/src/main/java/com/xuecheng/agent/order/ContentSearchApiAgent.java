package com.xuecheng.agent.order;

import com.xuecheng.api.search.model.dto.CoursePubIndexDTO;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import com.xuecheng.common.domain.response.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p></p>
 *
 * @Description:
 */
@FeignClient(value = XcFeignServiceNameList.XC_SEARCH_SERVICE)
public interface ContentSearchApiAgent {

    @GetMapping("search/l/course-index/{coursePubId}")
    RestResponse<CoursePubIndexDTO> getCoursePubIndexById4s(@PathVariable Long coursePubId);


}
