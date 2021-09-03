package com.demo.wxpay.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Package: com.demo.wxpay.utils
 * @author: jt
 * @date: 2021-08-31   14:36
 * @Description: 微信支付通用
 */
@Component
@Slf4j
public class WxPayUtils implements InitializingBean {

    @Value("${wxPay.appid}")
    private String appId;

    @Value("${wxPay.partner}")
    private String partner;

    @Value("${wxPay.partnerkey}")
    private String partnerKey;

    @Value("${wxPay.notifyurl}")
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
    public static String createorderidByuuid() {
        // 1.开头两位，标识业务代码或机器代码（可变参数）
        String machineId = "DD";

        // 2.中间四位整数，标识日期
        SimpleDateFormat sdf = new SimpleDateFormat("mmssS");
        String dayTime = sdf.format(new Date());
        // 3.生成uuid的hashCode值
        int hashCode = UUID.randomUUID().toString().hashCode();
        // 4.可能为负数
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        // 5.算法处理: 0-代表前面补充0; 10-代表长度为10; d-代表参数为正数型
        String value = machineId + dayTime + String.format("%010d", hashCode);
        System.out.println(value);

        return value;
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
