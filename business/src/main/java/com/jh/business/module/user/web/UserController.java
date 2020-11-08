package com.jh.business.module.user.web;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.jh.business.module.user.domain.SysUser;
import com.jh.business.module.user.domain.SysUserDetails;
import com.jh.business.module.user.service.SysUserService;
import com.jh.common.domain.AjaxResult;
import com.jh.common.redis.template.RedisRepository;
import com.jh.common.utils.RegularExpressionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @Author: LinJH
 * @Date: 2020/10/27 17:17
 * @Version:0.0.1
 */
@RestController
@Api(tags = "用户模块api")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private SysUserService sysUserSerivce;

    @Autowired
    private DefaultKaptcha captchaProducer;



    @ApiOperation(value = "获取token中的认证对象", notes = "")
    @GetMapping(value = "/info")
    public AjaxResult info() {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserSerivce.getById(sysUserDetails.getId());
        return AjaxResult.success(sysUser);
    }


    @ApiOperation(value = "注册新用户", notes = "")
    @PostMapping("/register")
    public AjaxResult register(@RequestBody SysUser sysUser){
        if(sysUser == null){
            return AjaxResult.error("用户信息对象为空");
        }
        if(StringUtils.isEmpty(sysUser.getEmail())){
            return AjaxResult.error("用户邮箱为空");
        }
        if(StringUtils.isEmpty(sysUser.getUsername())){
            return AjaxResult.error("用户名为空");
        }
        if(StringUtils.isEmpty(sysUser.getPassword())){
            return AjaxResult.error("用户密码为空");
        }

        if(!RegularExpressionUtils.check(RegularExpressionUtils.EMAIL_PATTERN,sysUser.getEmail())){
            return AjaxResult.error("用户邮箱输入格式不合法");
        }

        if(!RegularExpressionUtils.check(RegularExpressionUtils.USERNAME_PATTERN,sysUser.getUsername())){
            return AjaxResult.error("用户名输入格式不合法");
        }

        if(!RegularExpressionUtils.check(RegularExpressionUtils.PASSWORD_PATTERN,sysUser.getPassword())){
            return AjaxResult.error("密码输入格式不合法");
        }
        return sysUserSerivce.registerUser(sysUser);
    }

    @ApiOperation(value = "图形验证码", notes = "")
    @GetMapping("/verifyCode")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String verifyCode = captchaProducer.createText();
            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
            BufferedImage challenge = captchaProducer.createImage(verifyCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

}
