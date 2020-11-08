package com.jh.business.module.email.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jh.business.module.email.domain.TValidate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮箱数据持久层
 * 
 * @author CL
 *
 */
@Mapper
public interface TValidateRepository extends BaseMapper<TValidate> {

}
