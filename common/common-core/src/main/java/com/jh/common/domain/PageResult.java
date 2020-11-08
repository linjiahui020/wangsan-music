package com.jh.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: LinJH
 * @Date: 2020/11/7 18:47
 * @Version:0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 5571183956639359987L;
    /**
     * 总数
     */
    private Long total;
    /**
     * 当前页结果集
     */
    private List<T> rows;
}
