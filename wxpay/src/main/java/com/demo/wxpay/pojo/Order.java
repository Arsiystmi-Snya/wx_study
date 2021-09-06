package com.demo.wxpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Package: com.demo.wxpay.pojo
 * @author: jt
 * @date: 2021-09-01   10:28
 * @Description: 订单信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    /**
     * 应用ID
     */
    private String appid;
    /**
     * 商户号
     */
    private String partener;
    /**
     * 商户key
     */
    private String partenerkey;
    /**
     * 回调地址
     */
    private String notifyUrl;
    /**
     * 订单号
     */
    private String outTradeNo;
    /**
     * 价格
     */
    private Integer totalFee;
    /**
     * 订单状态
     * 0 表示未支付，1 表示已支付
     */
    private Integer attach;


    public Order(String appid, String partener, String partenerkey) {
        this.appid = appid;
        this.partener = partener;
        this.partenerkey = partenerkey;
    }
    // public String setOutTradeNo(String outTradeNo) {
    //     outTradeNo = WxPayUtils.createorderidByuuid();
    //     this.outTradeNo = outTradeNo;
    //     return outTradeNo;
    // }
}
