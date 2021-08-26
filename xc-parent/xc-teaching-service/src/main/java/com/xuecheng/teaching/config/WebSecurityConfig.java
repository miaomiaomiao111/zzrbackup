package com.xuecheng.teaching.config;


import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestErrorResponse;
import com.xuecheng.common.util.HttpUtil;
import com.xuecheng.teaching.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TokenAuthenticationFilter tokenAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(
                        "/workRecord/**", "/courseTeacher",
                        "/teacher/phone/*",
                        "/work"
                        ).authenticated()
                .anyRequest().permitAll()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 基于token，所以不需要session
.and().exceptionHandling().authenticationEntryPoint(((req, resp, exp) -> HttpUtil.writerError(new RestErrorResponse(CommonErrorCode.E_403000), resp)));
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
