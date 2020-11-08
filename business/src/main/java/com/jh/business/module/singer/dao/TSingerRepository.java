package com.jh.business.module.singer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jh.business.module.singer.domain.TSinger;
import com.jh.business.module.user.domain.SysAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: LinJH
 * @Date: 2020/11/7 11:05
 * @Version:0.0.1
 */
@Mapper
public interface TSingerRepository extends BaseMapper<TSinger> {

    public TSinger selectIdAndNameById(@Param("id") Long id);
}
