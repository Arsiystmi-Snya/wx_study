package com.demo.wxpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Package: com.demo.wxpay.pojo
 * @author: jt
 * @date: 2021-09-02   11:01
 * @Description: 返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    /**
     * 返回数据
     */
    private Object object;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 状态码
     */
    private Integer status;

}
