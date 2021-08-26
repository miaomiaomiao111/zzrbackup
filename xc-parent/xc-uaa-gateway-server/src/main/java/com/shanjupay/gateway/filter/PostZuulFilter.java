package com.shanjupay.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xuecheng.common.util.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.RibbonHttpResponse;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class PostZuulFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(PostZuulFilter.class);


    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 3;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = String.valueOf(ctx.get("requestURI"));
        if (requestURI.contains("abcdefghijklmnopqrstuvwxyz"))
        {
            //不需要处理的URL请求，返回false
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String requestURI = request.getRequestURI();


        InputStream stream = RequestContext.getCurrentContext().getResponseDataStream();
        try {
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            ctx.setResponseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object contentType = null;
        Object zuulResponse = RequestContext.getCurrentContext().get("zuulResponse");
        if (zuulResponse != null) {
            RibbonHttpResponse resp = (RibbonHttpResponse) zuulResponse;
            contentType = resp.getHeaders().getContentType();
        }

        Object requestIdObj = request.getAttribute("requestId");
        Object startTimeObj = request.getAttribute("startTime");

        long startTime = 0L;

        if(startTimeObj != null){
            startTime = (long)startTimeObj;
        }

        long executeTime = 0L;

        if(startTime > 0){
            executeTime = System.currentTimeMillis() - startTime;
        }

        ctx.getResponse().setCharacterEncoding("utf-8");

        logger.info("[{}][{}][{}][{}][{}][{}][{}][{}] response body : {}",
                request.getMethod(),
                requestURI,
                IPUtil.getIpAddr(request),
                request.getHeader("User-Agent"),
                contentType ,
                requestIdObj,
                executeTime,
                ctx.getResponse().getStatus(),
                ctx.getResponseBody());

        return null;
    }


}
