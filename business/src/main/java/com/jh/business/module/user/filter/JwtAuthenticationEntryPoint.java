package com.jh.business.module.user.filter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: LinJH
 * @Date: 2020/11/2 20:13
 * @Version:0.0.1
 */
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
////
////    @Override
////    public void commence(HttpServletRequest request,
////                         HttpServletResponse response,
////                         AuthenticationException authException) throws IOException {
////        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException==null?"Unauthorized":authException.getMessage());
////    }
//}
