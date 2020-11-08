package com.jh.business.module.user.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jh.business.properties.JwtSecurityProperties;
import com.jh.business.module.user.utils.JwtTokenUtils;
import com.jh.common.utils.ResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

/**
 * 登出成功处理类
 * 
 * @author CL
 *
 */
@Slf4j
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		// 添加到黑名单
		String token = request.getHeader(JwtSecurityProperties.tokenHeader);
		JwtTokenUtils.addBlackList(token);

		log.info("用户{}登出成功，Token信息已保存到Redis的黑名单中", JwtTokenUtils.getUserNameByToken(token));

		SecurityContextHolder.clearContext();
		ResponseUtils.responseJson(response, ResponseUtils.response(200, "登出成功", null));
	}
}
