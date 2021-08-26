package com.shanjupay.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;


@Configuration
public class ResouceServerConfig {

    public static final String RESOURCE_ID = "xuecheng-resource";

    private AuthenticationEntryPoint point = new RestOAuth2AuthExceptionEntryPoint();
    private RestAccessDeniedHandler handler = new RestAccessDeniedHandler();

    /**
     * 统一认证中心 资源拦截
     */
    @Configuration
    @EnableResourceServer
    public class UAAServerConfig extends ResourceServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/uaa/druid/**").denyAll()
                    .antMatchers("/uaa/**").permitAll();
        }

    }



    /********************************学成在线2.0 ResourceServer配置***********************************/
    /**
     * 因为学成在线 每个微服务都包含有所有接口，不需要登陆的、需要登陆的、需要权限的。
     * 在网关层进行接入端拦截，
     * 在后面的微服务去进行具体用户鉴权
     */
    @Configuration
    @EnableResourceServer
    public class XCServiceServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
            resources.authenticationEntryPoint(point).accessDeniedHandler(handler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            String[] xcContextPaths = new String[]{"/user/**", "/content/**", "/teaching/**", "/media/**",  "/learning/**",  "/order/**"
                    ,  "/comment/**",  "/search/**",  "/system/**"};
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/*/swagger-ui.html").denyAll()
                    .antMatchers("/*/druid/**").denyAll()
                    //.antMatchers("/content/common/**").authenticated()
                    .antMatchers(xcContextPaths).permitAll();
                    //.access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_XC_API')"); 由于公开URL没有指定规则，因此无法对接入端进行有效拦截

        }

    }


}
