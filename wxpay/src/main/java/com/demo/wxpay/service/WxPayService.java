package com.demo.wxpay.service;

import com.demo.wxpay.pojo.Order;

import java.util.Map;

/**
 * @Package: com.demo.wxpay.service
 * @author: jt
 * @date: 2021-09-01   11:06
 * @Description:
 */
public interface WxPayService {

    Map<String, String> createPayQRcode(int total_fee, String out_trade_no);

    Map<String, String> queryOrderStatus(String outTradeNo) throws Exception;

    /**
     * 生成微信的回调url
     * @param order
     * @return
     */
    String createPayQrCode(Order order);
}
