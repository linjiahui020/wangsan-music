package com.jh.business.module.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: LinJH
 * @Date: 2020/11/2 21:35
 * @Version:0.0.1
 */
@Data
@TableName("sys_user")
public class SysUser {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态（0-正常，1-删除，2-禁用）
     */
    private String status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户头像url
     */
    private String imgUrl;
}
