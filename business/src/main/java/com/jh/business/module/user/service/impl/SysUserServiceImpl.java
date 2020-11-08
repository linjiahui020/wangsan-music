package com.jh.business.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.business.module.user.dao.SysUserRepository;
import com.jh.business.module.user.domain.SysAuth;
import com.jh.business.module.user.domain.SysRole;
import com.jh.business.module.user.domain.SysUser;
import com.jh.business.module.user.service.SysUserService;
import com.jh.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统用户Service实现
 * @Author: LinJH
 * @Date: 2020/11/2 21:49
 * @Version:0.0.1
 */
@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserRepository, SysUser> implements SysUserService {

    @Autowired
    private PasswordEncoder encoder;

    /**
     * 根据用户名称查询用户信息
     *
     * @param username 用户名称
     * @return
     */
    @Override
    public SysUser findUserByUserName(String username) {
        return this.baseMapper.selectOne(
                new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username).ne(SysUser::getStatus, "1"));
    }

    /**
     * 根据邮箱或用户名查询用户信息
     *
     * @param username 邮箱或用户名
     * @return
     */
    @Override
    public SysUser findUserByEmailOrUsername(String username) {
        return this.baseMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getEmail,username).or().eq(SysUser::getUsername,username).ne(SysUser::getStatus,"1"));
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<SysRole> findRoleByUserId(Long userId) {
        return this.baseMapper.findRoleByUserId(userId);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<SysAuth> findAuthByUserId(Long userId) {
        return this.baseMapper.findAuthByUserId(userId);
    }

    /**
     * 注册新用户
     * @param sysUser
     */
    @Override
    public AjaxResult registerUser(SysUser sysUser){
        Integer tempCount;
        tempCount = this.baseMapper.selectCount(new QueryWrapper<SysUser>().eq("username",sysUser.getUsername()));
        if(tempCount != null &&  tempCount > 0){
            return AjaxResult.error("用户名已存在");
        }
        tempCount = this.baseMapper.selectCount(new QueryWrapper<SysUser>().eq("email",sysUser.getEmail()));
        if(tempCount != null &&  tempCount > 0){
            return AjaxResult.error("邮箱已存在");
        }
        //加密
        sysUser.setPassword(encoder.encode(sysUser.getPassword()));
        return this.baseMapper.insert(sysUser) == 0 ? AjaxResult.error("注册失败") : AjaxResult.success("注册成功");
    }
}
