package com.jh.business.module.music.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.business.module.music.dao.TSongRepository;
import com.jh.business.module.music.domain.TSong;
import com.jh.business.module.music.service.TSongService;
import com.jh.business.module.singer.dao.TSingerRepository;
import com.jh.business.module.singer.domain.TSinger;
import com.jh.business.module.singer.service.TSingerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: LinJH
 * @Date: 2020/11/7 15:48
 * @Version:0.0.1
 */
@Service
@Transactional
public class TSongServiceImpl extends ServiceImpl<TSongRepository, TSong> implements TSongService {
}
