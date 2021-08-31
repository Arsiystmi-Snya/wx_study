package com.demo.wxpay.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Package: com.demo.wxpay.utils
 * @author: jt
 * @date: 2021-08-31   14:36
 * @Description: 微信支付通用
 */
@Component
@Slf4j
public class WxPayUtils implements InitializingBean {

    @Value("${wx.pay.app_id}")
    private String appId;

    @Value("${wx.pay.partner}")
    private String partner;

    @Value("${wx.pay.partnerkey}")
    private String partnerKey;

    @Value("${wx.pay.notifyurl}")
    private String notifyUrl;

    /**
     * 公众账号id
     */
    public static String wx_pay_appId;
    /**
     * 商户号
     */
    public static String wx_pay_partner;
    /**
     * 商户key
     */
    public static String wx_pay_partnerKey;
    /**
     * 回调地址
     */
    public static String wx_pay_notifyUrl;

    @Override
    public void afterPropertiesSet() throws Exception {
        wx_pay_appId = appId;
        wx_pay_partner = partner;
        wx_pay_partnerKey = partnerKey;
        wx_pay_notifyUrl = notifyUrl;
    }

    /**
     * 生成订单号
     * @return
     */
    public static String createOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate = sdf.format(new Date());
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        log.info("=================================" + newDate + result);
        return newDate + result;
    }

    /**
     * 金额 元 转化为 分
     * @param amount
     * @return
     */
    public static String changeMoney(String amount) {
        if (amount == null) {
            return "支付金额为空";
        }

        // 金额转化为分，处理有$或者￥的
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");
        int index = currency.indexOf(".");
        int length = currency.length();

        Long amLong = 0L;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        log.info("=======================" + amLong.toString());
        return amLong.toString();

    }

}
