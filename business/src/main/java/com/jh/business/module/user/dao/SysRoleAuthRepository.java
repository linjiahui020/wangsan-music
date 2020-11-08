package com.jh.business.module.user.dao;

import com.jh.business.module.user.domain.SysRoleAuth;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 系统角色权限Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysRoleAuthRepository extends BaseMapper<SysRoleAuth> {

}
