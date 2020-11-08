package com.jh.business.module.email.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.business.module.email.dao.TValidateRepository;
import com.jh.business.module.user.domain.SysUser;
import com.jh.business.module.email.domain.TValidate;
import com.jh.business.module.email.service.TValidateService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: LinJH
 * @Date: 2020/11/8 10:29
 * @Version:0.0.1
 */
@Service
@Transactional
public class TValidateServiceImpl extends ServiceImpl<TValidateRepository, TValidate> implements TValidateService {

    @Autowired
    private JavaMailSender javaMailSender;


    /**
     * 发送邮件：@Async进行异步调用发送邮件接口
     * @param email
     */
    @Override
    @Async
    public void sendPasswordResetEmail(MimeMessage email){
        javaMailSender.send(email);
    }

    /**
     * 在t_validate表中插入一条validate记录，userid，email属性来自sys_user表，token由UUID生成
     * @param tValidate
     * @param sysUser
     * @param token
     * @return
     */
    @Override
    public int insertNewResetRecord(TValidate tValidate, SysUser sysUser,String token){
        tValidate.setUserId(sysUser.getId());
        tValidate.setEmail(sysUser.getEmail());
        tValidate.setResetToken(token);
        tValidate.setType("passwordReset");
        tValidate.setGmtCreate(new Date());
        tValidate.setGmtModified(new Date());
        return this.baseMapper.insert(tValidate);
    }

    /**
     * t_validate表中，通过token查找重置申请记录
     * @param token
     * @return
     */
    @Override
    public TValidate findUserByResetToken(String token){
        return this.baseMapper.selectOne(new QueryWrapper<TValidate>().eq("reset_token",token));
    }

    /**
     * 验证是否发送重置邮件：每个email的重置密码每日请求上限为requestPerDay次，与上一次的请求时间间隔为interval分钟。
     * @param email
     * @param requestPerDay
     * @param interval
     * @return
     */
    @Override
    public boolean sendValidateLimitation(String email, long requestPerDay, long interval){
        List<TValidate> validateList = this.baseMapper.selectList(new QueryWrapper<TValidate>().eq("email",email));
        // 若查无记录，意味着第一次申请，直接放行
        if (validateList.isEmpty()) {
            return true;
        }
        // 有记录，则判定是否频繁申请以及是否达到日均请求上线
        long countTodayValidation = validateList.stream().filter(x-> DateUtils.isSameDay(x.getGmtModified(), new Date())).count();
        Optional validate = validateList.stream().map(TValidate::getGmtModified).max(Date::compareTo);
        Date dateOfLastRequest = new Date();
        if (validate.isPresent()){
            dateOfLastRequest = (Date) validate.get();
        }
        long intervalForLastRequest = new Date().getTime() - dateOfLastRequest.getTime();

        return countTodayValidation <= requestPerDay && intervalForLastRequest >= interval * 60 * 1000;
    }

    /**
     * 验证连接是否失效：链接有两种情况失效 1.超时 2.最近请求的一次链接自动覆盖之前的链接（待看代码）
     * @param email
     * @param requestPerDay
     * @param interval
     * @return
     */
    @Override
    public boolean validateLimitation(String email, long requestPerDay, long interval, String token){
        List<TValidate> validateList = this.baseMapper.selectList(new QueryWrapper<TValidate>().eq("email",email));
        // 有记录才会调用该函数，只需判断是否超时
        Optional validate = validateList.stream().map(TValidate::getGmtModified).max(Date::compareTo);
        Date dateOfLastRequest = new Date();
        if (validate.isPresent()) dateOfLastRequest = (Date) validate.get();
        long intervalForLastRequest = new Date().getTime() - dateOfLastRequest.getTime();

        Optional lastRequestToken = validateList.stream().filter(x-> x.getResetToken().equals(token)).map(TValidate::getGmtModified).findAny();
        Date dateOfLastRequestToken = new Date();
        if (lastRequestToken.isPresent()) {
            dateOfLastRequestToken = (Date) lastRequestToken.get();
        }
        return intervalForLastRequest <= interval * 60 * 1000 && dateOfLastRequest == dateOfLastRequestToken;
    }

}
