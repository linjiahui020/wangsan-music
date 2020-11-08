package com.jh.business.module.singer.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: LinJH
 * @Date: 2020/11/7 10:57
 * @Version:0.0.1
 */
@TableName("t_singer")
@Data
public class TSinger {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 歌手名字
     */
    private String singerName;

    /**
     * 歌手简介
     */
    private String description;

    /**
     * 歌手主页头像路径
     */
    private String headImgUrl;
}
