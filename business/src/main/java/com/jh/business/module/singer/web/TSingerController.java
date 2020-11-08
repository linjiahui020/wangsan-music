package com.jh.business.module.singer.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jh.business.module.music.domain.TSong;
import com.jh.business.module.music.service.TSongService;
import com.jh.business.module.singer.domain.TSinger;
import com.jh.business.module.singer.service.TSingerService;
import com.jh.common.domain.AjaxResult;
import com.jh.common.domain.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * @Date: 2020/11/7 11:12
 * @Version:0.0.1
 */
@Api(tags = "歌手模块api")
@RestController
@RequestMapping("/singer")
public class TSingerController {

    @Autowired
    private TSingerService tSingerService;

    @Autowired
    private TSongService tSongService;

    /**
     * 根据歌手名字获取歌曲
     * @return
     */
    @ApiOperation(value = "根据歌手名字获取歌曲(含分页)", notes = "根据歌手名字获取歌曲(含分页)")
    @GetMapping("/list")
    public AjaxResult getList(@ApiParam("参数包含page,limit,singerName(歌手名字)")@RequestParam Map<String,Object> params){
        Page<TSong> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        TSinger singer = tSingerService.getOne(new QueryWrapper<TSinger>().eq("singer_name",params.get("singerName")));
        if(singer == null){
            return AjaxResult.success("该歌手暂时还没歌曲(ಥ﹏ಥ)",null);
        }
        QueryWrapper<TSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("singer_id",singer.getId());
        IPage<TSong> tSongPage = tSongService.page(page,queryWrapper);
        return AjaxResult.success(PageResult.<TSong>builder().total(tSongPage.getTotal()).rows(tSongPage.getRecords()).build());
    }

    /**
     * 返回歌手信息
     * @return
     */
    @ApiOperation(value = "根据歌手id返回歌手信息", notes = "根据歌手id返回歌手信息")
    @GetMapping("/info")
    public AjaxResult getInfoByName(@ApiParam("参数包含歌手id")@RequestParam Map<String,Object> params){
        if(params.containsKey("id")){
            return AjaxResult.success(tSingerService.getOne(new QueryWrapper<TSinger>().eq("id",params.get("id"))));
        }
        return AjaxResult.error("请传入歌手id");
    }

}
