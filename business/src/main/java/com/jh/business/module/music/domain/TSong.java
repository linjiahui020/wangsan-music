package com.jh.business.module.music.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jh.business.module.singer.domain.TSinger;
import lombok.Data;

/**
 * @Author: LinJH
 * @Date: 2020/11/7 11:22
 * @Version:0.0.1
 */
@TableName(value = "t_song" , resultMap = "tSongMap")
@Data
public class TSong {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 对应歌手id
     */
    private Long singerId;

    /**
     * 对应歌手对象
     */
    private TSinger tSinger;

    /**
     * 歌曲名字
     */
    private String songName;

    /**
     * 歌曲图片
     */
    private String songImg;

    /**
     * 音乐mp3路径
     */
    private String musicUrl;

    /**
     * mv路径
     */
    private String mvUrl;
}
