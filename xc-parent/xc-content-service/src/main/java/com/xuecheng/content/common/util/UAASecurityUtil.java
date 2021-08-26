package com.xuecheng.content.common.util;


import com.xuecheng.agent.content.TeachingApiAgent;
import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.common.domain.uaa.LoginUser;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

/***
 * 获取当前登录用户信息
 * 前端配置token，后续每次请求并通过Header方式发送至后端
 * 在后端controller中通过 UAASecurityUtil.getUser() 方法获取当前登录用户信息
 */
public class UAASecurityUtil {

	public static LoginUser getUser() {
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		return (LoginUser)request.getAttribute(LoginUser.REQUEST_USER);
	}

	/**
	 * 返回当前登录账号所在教学机构完整信息
	 * @return
	 */
	public static CompanyDTO getCompany(){
		Long tenantId = getTenantId();
		if(tenantId == null){
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		TeachingApiAgent teachingApiAgent = wac.getBean(TeachingApiAgent.class);
		return teachingApiAgent.getCompInfoDetail(tenantId);
	}


	/**
	 *  返回当前登录账号所在教学机构ID
	 * @return
	 */
	public static Long getCompanyId(){
		return getCompany().getCompanyId();
	}

	public static Long getTenantId() {
		LoginUser loginUser = getUser();
		if(loginUser == null){
			throw new RuntimeException("解析令牌获得用户数据失败");
		}
		return loginUser.getTenantId();
	}

}
