package com.jh.business.module.email.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jh.business.module.email.domain.TValidate;
import com.jh.business.module.email.service.TValidateService;
import com.jh.business.module.user.domain.SysUser;
import com.jh.business.module.user.service.SysUserService;
import com.jh.common.domain.AjaxResult;
import com.jh.common.utils.RegularExpressionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: LinJH
 * @Date: 2020/11/8 10:55
 * @Version:0.0.1
 */
@Api(tags = "利用邮件修改密码功能api")
@RestController
@RequestMapping(value = "/validate")
public class ValidateController {

    @Autowired
    private TValidateService validateService;

    @Autowired
    private SysUserService userService;

    //发送邮件的类
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder encoder;

    //这里使用的是我们已经在配置问价中固定了的变量值,也就是通过这个邮箱向目标邮箱发送重置密码的邮件
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送忘记密码邮件请求，每日申请次数不超过20次，每次申请间隔不低于1分钟
     * @param email
     * @param request
     * @return
     */
    @ApiOperation(value = "发送忘记密码邮件(每日申请次数不超过20次，每次申请间隔不低于1分钟)", notes = "发送忘记密码邮件")
    @RequestMapping(value = "/sendEmail", method = {RequestMethod.POST})
    public AjaxResult sendValidationEmail(@ApiParam("邮箱地址") @RequestParam("email") String email,
                                          HttpServletRequest request) throws MessagingException {
        SysUser sysUser = userService.getOne(new QueryWrapper<SysUser>().eq("email",email));
        if (sysUser == null){
            return AjaxResult.error("该邮箱所属用户不存在");
        }else {
            if (validateService.sendValidateLimitation(email, 20,1)){
                // 若允许重置密码，则在t_validate表中插入一行数据，带有token
                TValidate tValidate = new TValidate();
                validateService.insertNewResetRecord(tValidate, sysUser, UUID.randomUUID().toString());
                // 设置邮件内容
                String appUrl = request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort();
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                // multipart模式
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setFrom(from);
                mimeMessageHelper.setSubject("重置密码");
                StringBuilder sb = new StringBuilder();
                sb.append("<html><head></head>");
                sb.append("<body><h1>点击下面的链接重置密码</h1>" +
                        "<a href = "+appUrl +"/validate/resetPassword?token="+tValidate.getResetToken()+">"+appUrl +"/validate/resetPassword?token=" +tValidate.getResetToken()+"</a></body>");
                sb.append("</html>");
                // 启用html
                mimeMessageHelper.setText(sb.toString(), true);
                validateService.sendPasswordResetEmail(mimeMessage);

//                SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
//                passwordResetEmail.setFrom(from);
//                passwordResetEmail.setTo(email);
//                passwordResetEmail.setSubject("忘记密码");
//                passwordResetEmail.setText("您正在申请重置密码，请点击此链接重置密码: \n" +appUrl + "/validate/resetPassword?token=" + validateDao.getResetToken());
//                validateService.sendPasswordResetEmail(passwordResetEmail);

                Map<String,Object> map1=new HashMap<>();
                map1.put("token",tValidate.getResetToken());
                map1.put("link",appUrl +"/validate/resetPassword?token="+tValidate.getResetToken());
                map1.put("message","邮件已经发送");
                return AjaxResult.success(map1);
            }
            return AjaxResult.error("操作过于频繁，请稍后再试！");
        }
    }

    /**
     * 将url的token和数据库里的token匹配，成功后便可修改密码，token有效期为5分钟
     * @param token
     * @param password
     * @param confirmPassword
     * @return
     */
    @ApiOperation(value = "重置密码,邮箱中的token有效时间为5分钟,每天每个用户最多发10次邮件", notes = "重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public AjaxResult resetPassword(@ApiParam("token") @RequestParam("token") String token,
                                    @ApiParam("密码") @RequestParam("password") String password,
                                    @ApiParam("密码确认") @RequestParam("confirmPassword") String confirmPassword){

        if(StringUtils.isEmpty(password)){
            return AjaxResult.error("密码不能为空");
        }

        if(StringUtils.isEmpty(confirmPassword)){
            return AjaxResult.error("确认密码不能为空");
        }

        if(!RegularExpressionUtils.check(RegularExpressionUtils.PASSWORD_PATTERN,password)){
            return AjaxResult.error("密码输入格式不合法");
        }

        if(!RegularExpressionUtils.check(RegularExpressionUtils.PASSWORD_PATTERN,confirmPassword)){
            return AjaxResult.error("确认密码输入格式不合法");
        }

        // 通过token找到validate记录
        TValidate tValidate= validateService.findUserByResetToken(token);

        if (tValidate == null){
            return AjaxResult.error("该重置请求不存在");
        }else {
            if (validateService.validateLimitation(tValidate.getEmail(), Long.MAX_VALUE, 5, token)){
                Long userId = tValidate.getUserId();
                if (password.equals(confirmPassword)) {
                    SysUser sysUser=new SysUser();
                    sysUser.setPassword(encoder.encode(password));
                    sysUser.setId(userId);
                    userService.updateById(sysUser);
                    return AjaxResult.success("成功重置密码");
                }else {
                    return AjaxResult.error("确认密码和密码不一致，请重新输入");
                }
            }else {
                return AjaxResult.error("该链接失效");
            }
        }
    }

}
