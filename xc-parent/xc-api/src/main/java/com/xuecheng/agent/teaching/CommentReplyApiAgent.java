//package com.xuecheng.agent.teaching;
//
//import com.xuecheng.agent.teaching.sentinel.CommentApiAgentFallBack;
//import com.xuecheng.agent.teaching.sentinel.CommentReplyApiAgentFallBack;
//import com.xuecheng.api.comment.model.CommentReplyDTO;
//import com.xuecheng.common.constant.XcFeignServiceNameList;
//import com.xuecheng.common.domain.response.RestResponse;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import javax.validation.Valid;
//
///**
// * 教学服务 远程调用回复评论接口
// *
// * @author zzr
// */
//@FeignClient(value = XcFeignServiceNameList.XC_COMMENT_SERVICE, fallback = CommentReplyApiAgentFallBack.class)
//public interface CommentReplyApiAgent {
//
//
//}
