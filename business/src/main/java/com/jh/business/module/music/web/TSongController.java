package com.jh.business.module.music.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jh.business.module.music.domain.TSong;
import com.jh.business.module.music.service.TSongService;
import com.jh.business.module.singer.domain.TSinger;
import com.jh.business.module.singer.service.TSingerService;
import com.jh.common.domain.AjaxResult;
import com.jh.common.domain.PageResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinJH
 * @Date: 2020/11/7 18:24
 * @Version:0.0.1
 */
@RestController
@RequestMapping("/music")
public class TSongController {
    @Autowired
    private TSongService tSongService;

    @Autowired
    private TSingerService tSingerService;

    /**
     * 根据搜索关键字获取歌曲
     * @return
     */
    @GetMapping("/search")
    public AjaxResult getList(@RequestParam Map<String,Object> params){
        Page<TSong> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<TSinger> singers = tSingerService.list(new QueryWrapper<TSinger>().like("singer_name",params.get("keyWord")));
        if(singers != null && !singers.isEmpty()){
            QueryWrapper<TSong> queryWrapper = new QueryWrapper<>();
            for(TSinger tSinger:singers){
                queryWrapper.eq("singer_id",tSinger.getId()).or();
            }
            IPage<TSong> tSongPage = tSongService.page(page,queryWrapper);
            return AjaxResult.success(PageResult.<TSong>builder().total(tSongPage.getTotal()).rows(tSongPage.getRecords()).build());
        }else{
            QueryWrapper<TSong> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("song_name",params.get("keyWord"));
            IPage<TSong> tSongPage = tSongService.page(page,queryWrapper);
            return AjaxResult.success(PageResult.<TSong>builder().total(tSongPage.getTotal()).rows(tSongPage.getRecords()).build());
        }
    }
}
