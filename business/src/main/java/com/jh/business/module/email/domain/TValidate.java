package com.jh.business.module.email.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_validate")
public class TValidate {

    /**
     * ID
     */
    @TableId
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 重置token
     */
    private String resetToken;

    /**
     * 类型
     */
    private String type;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}