package com.demo.wxpay.pojo;

import com.demo.wxpay.utils.WxPayUtils;
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
    private Integer id;
    /**
     * 应用ID
     */
    private String appid;
    /**
     * 商户号
     */
    private String partner;
    /**
     * 商户key
     */
    private String partnerkey;
    /**
     * 回调地址
     */
    private String notifyurl;
    /**
     * 商品描述
     */
    private String description;
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

    public String setOutTradeNo(String outTradeNo) {
        outTradeNo = WxPayUtils.createorderidByuuid();
        this.outTradeNo = outTradeNo;
        return outTradeNo;
    }

    // TODO
    // public Integer getAttach() {
    //     return attach;
    // }
    //
    // public void setAttach() {
    //     this.attach = 1;
    // }

    // public void setAttach(Integer attach) {
    //     this.attach = attach;
    // }
    //
    // public Integer setAttach() {
    //     attach = String.valueOf(0);
    //     return Integer.valueOf(attach);
    // }
    //
    // public Integer getAttach() {
    //     return Integer.valueOf(attach);
    // }

}
