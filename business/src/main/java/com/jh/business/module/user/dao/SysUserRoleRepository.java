package com.jh.business.module.user.dao;

import com.jh.business.module.user.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 系统用户角色Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysUserRoleRepository extends BaseMapper<SysUserRole> {

}
