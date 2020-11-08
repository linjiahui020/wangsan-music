package com.jh.business.module.user.dao;

import java.util.List;

import com.jh.business.module.user.domain.SysAuth;
import com.jh.business.module.user.domain.SysRole;
import com.jh.business.module.user.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 系统用户Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysUserRepository extends BaseMapper<SysUser> {

	/**
	 * 根据用户ID查询角色
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<SysRole> findRoleByUserId(Long userId);

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<SysAuth> findAuthByUserId(Long userId);
}
