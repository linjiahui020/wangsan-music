package com.jh.business.module.music.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jh.business.module.music.domain.TSong;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: LinJH
 * @Date: 2020/11/7 12:06
 * @Version:0.0.1
 */
@Mapper
public interface TSongRepository extends BaseMapper<TSong> {
}
