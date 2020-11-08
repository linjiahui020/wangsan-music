package com.jh.business.module.user.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jh.business.module.user.domain.SysUserDetails;
import com.jh.business.module.user.utils.JwtTokenUtils;
import com.jh.common.utils.AccessAddressUtils;
import com.jh.common.utils.ResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录成功处理类
 * 
 * @author CL
 *
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

	private static final String DEFAULT_IMG_URL = "http://47.115.72.55:8888/group1/M00/00/00/rBePbV-iXFWAa_KAAAC7rBvllWM205.jpg";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();
		// 获得请求IP
		String ip = AccessAddressUtils.getIpAddress(request);
		sysUserDetails.setIp(ip);
		sysUserDetails.setImgUrl(DEFAULT_IMG_URL);
		String token = JwtTokenUtils.createAccessToken(sysUserDetails);

		// 保存Token信息到Redis中
		JwtTokenUtils.setTokenInfo(token, sysUserDetails.getUsername(), ip);
		
		log.info("用户{}登录成功，Token信息已保存到Redis", sysUserDetails.getUsername());

		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put("token", token);
		ResponseUtils.responseJson(response, ResponseUtils.response(200, "登录成功", tokenMap));
	}
}
