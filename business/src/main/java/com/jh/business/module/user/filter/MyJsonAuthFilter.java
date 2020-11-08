package com.jh.business.module.user.filter;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jh.common.domain.AjaxResult;
import com.jh.common.utils.RegularExpressionUtils;
import com.jh.common.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: LinJH
 * @Date: 2020/11/2 23:56
 * @Version:0.0.1
 */
@Slf4j
public class MyJsonAuthFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(!StringUtils.equalsIgnoreCase("POST",request.getMethod()) || !request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            ResponseUtils.responseJson(response, ResponseUtils.response(500, "必须是post请求方式和json数据格式",null));
            return null;
        }
        String username = null;
        String password = null;
        String code = null;
        try {
            String verifyCode = request.getSession().getAttribute("verifyCode").toString();
            Map<String,String> map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            username = map.get("username") == null ? map.get("email") : map.get("username");
            password = map.get("password");
            code = map.get("code");
            if(StringUtils.isEmpty(username)){
                ResponseUtils.responseJson(response, ResponseUtils.response(500, "用户名或邮箱不能为空",null));
            }
            if(StringUtils.isEmpty(password)){
                ResponseUtils.responseJson(response, ResponseUtils.response(500, "密码不能为空", null));
            }
            if(StringUtils.isEmpty(code)){
                ResponseUtils.responseJson(response,ResponseUtils.response(500,"验证码不能为空",null));
            }

            if(!RegularExpressionUtils.check(RegularExpressionUtils.USERNAME_PATTERN,username) && !RegularExpressionUtils.check(RegularExpressionUtils.EMAIL_PATTERN,username)){
                ResponseUtils.responseJson(response, ResponseUtils.response(500, "用户名或邮箱输入格式不合法", null));
            }

            if(!RegularExpressionUtils.check(RegularExpressionUtils.PASSWORD_PATTERN,password)){
                ResponseUtils.responseJson(response, ResponseUtils.response(500, "密码输入格式不合法", null));
            }

            if(!StringUtils.equals(code,verifyCode)){
                ResponseUtils.responseJson(response, ResponseUtils.response(500, "验证码错误", null));
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
            setDetails(request,token);
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.attemptAuthentication(request, response);
    }

}
