package com.shanjupay.gateway.filter;


import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.common.util.EncryptUtil;
import com.xuecheng.common.util.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class AuthFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }



        String requestId = genNo();
        request.setAttribute("requestId",requestId);
        request.setAttribute("startTime",System.currentTimeMillis());

        logger.info("[{}][{}][{}][{}][{}][{}] request params : {}",
                request.getMethod(),
                requestURI,
                IPUtil.getIpAddr(request),
                request.getHeader("User-Agent"),
                request.getContentType(),
                requestId,
                params);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof OAuth2Authentication)){ // 无token访问网关内资源的情况，目前仅有uaa服务直接暴露
            return null;
        }

        OAuth2Authentication oauth2Authentication  = (OAuth2Authentication)authentication;
        Authentication userAuthentication = oauth2Authentication.getUserAuthentication();

        Map<String,String> jsonToken = new HashMap<>(oauth2Authentication.getOAuth2Request().getRequestParameters());
        if(userAuthentication != null){
            jsonToken.put("user_name",userAuthentication.getName());
        }

        //request.getParameterMap();// 关键步骤，一定要get一下,下面这行代码才能取到值
        //Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();
        //if (requestQueryParams == null) {
        //    requestQueryParams = new HashMap<>();
        //}
        //List<String> arrayList = new ArrayList<>();
        //arrayList.add(EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));
        //requestQueryParams.put("json-token", arrayList);
        //ctx.setRequestQueryParams(requestQueryParams);
        ctx.addZuulRequestHeader("jsonToken", EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));

        //TODO: web端续期逻辑，主动登出拦截
        return null;
    }

    private String genNo() {
        return UUID.randomUUID().toString();
    }


}
