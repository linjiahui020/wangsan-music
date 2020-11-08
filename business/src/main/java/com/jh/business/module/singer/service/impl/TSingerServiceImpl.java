package com.jh.business.module.singer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.business.module.singer.dao.TSingerRepository;
import com.jh.business.module.singer.domain.TSinger;
import com.jh.business.module.singer.service.TSingerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: LinJH
 * @Date: 2020/11/7 11:10
 * @Version:0.0.1
 */
@Service
@Transactional
public class TSingerServiceImpl extends ServiceImpl<TSingerRepository, TSinger> implements TSingerService {
}
