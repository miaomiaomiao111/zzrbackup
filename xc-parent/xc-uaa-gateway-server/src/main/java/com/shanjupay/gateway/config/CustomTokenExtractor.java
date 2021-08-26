package com.shanjupay.gateway.config;

import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class CustomTokenExtractor extends BearerTokenExtractor {



    @Override
    protected String extractHeaderToken(HttpServletRequest request) {
        Enumeration headers = request.getHeaders("Authorization");

        String value;
        if (!headers.hasMoreElements()) {
            return null;
        }
        value = (String)headers.nextElement();


        String authHeaderValue = value.trim();
        request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, "Bearer");
        int commaIndex = authHeaderValue.indexOf(44);
        if (commaIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }

        return authHeaderValue;
    }



}

