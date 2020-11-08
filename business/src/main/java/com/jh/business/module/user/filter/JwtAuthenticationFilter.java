package com.jh.business.module.user.filter;

import com.alibaba.druid.util.StringUtils;
import com.jh.business.properties.JwtSecurityProperties;
import com.jh.business.module.user.domain.SysUserDetails;
import com.jh.business.module.user.utils.JwtTokenUtils;
import com.jh.common.utils.AccessAddressUtils;
import com.jh.common.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: LinJH
 * @Date: 2020/11/2 18:56
 * @Version:0.0.1
 * 认证
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {


        // 取出Token
        String token = request.getHeader(JwtSecurityProperties.tokenHeader);

        if (token != null && token.startsWith(JwtSecurityProperties.tokenPrefix)) {
            // 是否在黑名单中
            if (JwtTokenUtils.isBlackList(token)) {
                ResponseUtils.responseJson(response, ResponseUtils.response(505, "Token已失效", "Token已进入黑名单"));
                return;
            }

            // 是否存在于Redis中
            if (JwtTokenUtils.hasToken(token)) {
                String ip = AccessAddressUtils.getIpAddress(request);
                String expiration = JwtTokenUtils.getExpirationByToken(token);
                String username = JwtTokenUtils.getUserNameByToken(token);

                // 判断是否过期
                if (JwtTokenUtils.isExpiration(expiration)) {
                    // 加入黑名单
                    JwtTokenUtils.addBlackList(token);

                    // 是否在刷新期内
                    String validTime = JwtTokenUtils.getRefreshTimeByToken(token);
                    if (JwtTokenUtils.isValid(validTime)) {
                        // 刷新Token，重新存入请求头
                        String newToke = JwtTokenUtils.refreshAccessToken(token);

                        // 删除旧的Token，并保存新的Token
                        JwtTokenUtils.deleteRedisToken(token);
                        JwtTokenUtils.setTokenInfo(newToke, username, ip);
                        response.setHeader(JwtSecurityProperties.tokenHeader, newToke);

                        log.info("用户{}的Token已过期，但为超过刷新时间，已刷新", username);

                        token = newToke;
                    } else {

                        log.info("用户{}的Token已过期且超过刷新时间，不予刷新", username);

                        // 加入黑名单
                        JwtTokenUtils.addBlackList(token);
                        ResponseUtils.responseJson(response, ResponseUtils.response(505, "Token已过期", "已超过刷新有效期"));
                        return;
                    }
                }

                SysUserDetails sysUserDetails = JwtTokenUtils.parseAccessToken(token);

                if (sysUserDetails != null) {
                    // 校验IP
                    if (!StringUtils.equals(ip, sysUserDetails.getIp())) {

                        log.info("用户{}请求IP与Token中IP信息不一致", username);

                        // 加入黑名单
                        JwtTokenUtils.addBlackList(token);
                        ResponseUtils.responseJson(response, ResponseUtils.response(505, "Token已过期", "可能存在IP伪造风险"));
                        return;
                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            sysUserDetails, sysUserDetails.getId(), sysUserDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
