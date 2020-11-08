package com.jh.business.module.user.dao;

import com.jh.business.module.user.domain.SysAuth;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 系统权限Dao
 * 
 * @author CL
 *
 */
@Mapper
public interface SysAuthRepository extends BaseMapper<SysAuth> {

}
