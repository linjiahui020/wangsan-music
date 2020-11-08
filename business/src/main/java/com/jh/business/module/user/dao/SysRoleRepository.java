package com.jh.business.module.user.dao;

import com.jh.business.module.user.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 系统角色Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysRoleRepository extends BaseMapper<SysRole> {

}
