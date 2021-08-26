package com.xuecheng.media.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuecheng.common.domain.response.RestErrorResponse;
import com.xuecheng.common.domain.uaa.LoginUser;
import com.xuecheng.common.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // 1.校验tenantId 和jsontoken
        String tenantId = httpServletRequest.getParameter("tenantId");//表示当前请求是来自哪个租户 由前端传过来
        log.info("tenantId:{}", tenantId);

        String json_token = httpServletRequest.getHeader(LoginUser.GATEWAY_JSON_TOKEN);//网关给后端微服务传的token令牌
        log.info("json__token:{}", json_token);

        if (StringUtils.isNotBlank(json_token) ) {
            // 2.base64解码 后判断有没有当前租户的权限信息
            String json = EncryptUtil.decodeUTF8StringBase64(json_token);
            Map tokenMap = JSON.parseObject(json, Map.class);
            Map payloadMap = null;
            Object payloadObj = tokenMap.get("payload");
            if (payloadObj instanceof String) {
                payloadMap = JSON.parseObject((String) payloadObj, Map.class);
            } else if (payloadObj instanceof Map) {
                payloadMap = (Map) payloadObj;
            } else {
                log.error("payload data error");
            }
            log.info("payloadMap:{}", JSON.toJSONString(payloadMap));
            /**
             * 对应学成在线，暂时不考虑多租户的问题
             *   学生的payload为空
             *   教师所属至少一个租户（教学结构）
             *   运营属于学成的根租户
             */
            Set<String> rolePrivileges = new HashSet<>();

            if (payloadMap != null && payloadMap.size() > 0) {
                Map tenantMap = (Map)payloadMap.get(tenantId); //获取当前租户的权限信息
                if(tenantMap == null){
                    tenantId = (String) payloadMap.keySet().iterator().next();
                    tenantMap  =(Map)payloadMap.get(tenantId);
                }


                // 3.取对应的 user_authorities信息 将权限放到几个arraylist集合中  不需要角色
                Map<String, JSONArray> userAuthMap = (Map<String, JSONArray>)tenantMap.get("user_authorities");
                log.info("userAuthMap:{}", JSON.toJSONString(userAuthMap));

                for (Map.Entry<String, JSONArray> entry : userAuthMap.entrySet()) {
                    List<String> privileges = JSONObject.parseArray(JSON.toJSONString(entry.getValue()), String.class);
                    rolePrivileges.addAll(privileges);
                }
                log.info("该租户 {} 拥有的权限最终个数{},分别是{}", tenantId, rolePrivileges.size(), rolePrivileges);
            }

            // 4.新建并填充authentication 使用security做后续的鉴权
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    tokenMap.get("user_name"), null, AuthorityUtils.createAuthorityList(rolePrivileges.toArray(new String[rolePrivileges.size()])));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            // 5.将authentication保存进安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);


            //5.设置LoginUser
            LoginUser user = new LoginUser();
            user.setPayload(payloadMap);
            user.setUsername((String)tokenMap.get("user_name"));
            user.setClientId((String)tokenMap.get("client_id"));
            user.setMobile((String)tokenMap.get("mobile"));
            if(tenantId != null){
                user.setTenantId(Long.parseLong(tenantId));
            }
            httpServletRequest.setAttribute(LoginUser.REQUEST_USER, user);

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    public void responseMessage(int code, String desc, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        // 由于全局异常处理器无法捕获filter中的异常信息 利用此map将异常信息直接响应到前端
        RestErrorResponse errorResponse = new RestErrorResponse(String.valueOf(code), desc);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().print(JSONObject.toJSON(errorResponse));
    }

}
