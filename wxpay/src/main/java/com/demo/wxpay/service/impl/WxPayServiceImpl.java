package com.demo.wxpay.service.impl;

import com.demo.wxpay.pojo.Order;
import com.demo.wxpay.service.WxPayService;
import com.demo.wxpay.utils.HttpClient;
import com.demo.wxpay.utils.WxPayUtils;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package: com.demo.wxpay.service.impl
 * @author: jt
 * @date: 2021-09-01   11:07
 * @Description:
 */
@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    /**
     * 生成支付二维码
     * @param totalFee   价格
     * @param outTradeNo 订单号
     * @return
     */
    @Override
    public Map<String, String> createPayQRcode(int totalFee, String outTradeNo) {

        HashMap<String, String> orderParams = new HashMap<>();
        orderParams.put("appid", WxPayUtils.wx_pay_appId);
        orderParams.put("mch_id", WxPayUtils.wx_pay_partner);
        // 随机字符串，保证签名不可预测
        orderParams.put("nonce_str", WXPayUtil.generateNonceStr());
        // 签名不需要，从xml加密参数中直接注入
        orderParams.put("body", "demo");
        // 订单号
        orderParams.put("out_trade_no", outTradeNo);
        // 金额
        orderParams.put("total_fee", String.valueOf(totalFee));
        // 项目的域名，终端ip
        orderParams.put("spbill_create_ip", "127.0.0.1");
        orderParams.put("notify_url", WxPayUtils.wx_pay_notifyUrl);
        // 支付类型，扫码支付
        orderParams.put("trade_type", "NATIVE");
        orderParams.put("attach", "1");

        try {
            // 发送请求，传递xml参数，微信支付提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // xml参数加密
            client.setXmlParam(WXPayUtil.generateSignedXml(orderParams, WxPayUtils.wx_pay_partnerKey));
            // is https 请求
            client.setHttps(true);
            // post 请求发送
            client.post();
            // 使用xml返回结果
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            new RedirectAttributesModelMap().addAttribute("xml", xml);

            log.info("====================订单的xml解析地址===================");
            System.out.println(xml);

            HashMap<String, String> map = new HashMap<>();
            map.put("outTradeNo", outTradeNo);
            map.put("totalFee", String.valueOf(totalFee));
            map.put("return_code", resultMap.get("return_code"));
            map.put("code_url", resultMap.get("code_url"));
            map.put("xml", xml);

            log.info("=======================返回数据解析===================");
            System.out.println(map);

            return map;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断是否完成支付
     * @param outTradeNo
     * @return
     */
    @Override
    public Map<String, String> queryOrderStatus(String outTradeNo) throws Exception {

        // 设置参数
        HashMap<String, String> orderParams = new HashMap<>();
        orderParams.put("appid", WxPayUtils.wx_pay_appId);
        orderParams.put("mch_id", WxPayUtils.wx_pay_partner);
        orderParams.put("out_trade_no", outTradeNo);
        orderParams.put("nonce_str", WXPayUtil.generateNonceStr());

        // 发送httpclient
        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        client.setXmlParam(WXPayUtil.generateSignedXml(orderParams, WxPayUtils.wx_pay_partnerKey));
        client.setHttps(true);
        client.post();

        // 得到订单数据
        String xml = client.getContent();
        Map<String, String> reusltMap = WXPayUtil.xmlToMap(xml);

        log.info("====================订单的xml解析地址===================");
        System.out.println(xml);

        // TODO 改变数据库中的数据等操作
        return reusltMap;
    }

    /**
     * 生成微信的回调url
     * @param order
     * @return url回调地址
     */
    @Override
    public String createPayQrCode(Order order) {
        HashMap<String, String> urlParams = new HashMap<>();
        urlParams.put("appid", order.getAppId());
        urlParams.put("mch_id", order.getMchId());
        // 随机字符串
        urlParams.put("nonce_str", WXPayUtil.generateNonceStr());
        urlParams.put("body", "微信扫码支付demo");
        // 订单号
        urlParams.put("out_trade_no", order.getOutTradeNo());
        // 金额，单位：分
        urlParams.put("total_fee", String.valueOf(order.getTotalFee()));
        urlParams.put("spbill_create_ip", "127.0.0.1");
        urlParams.put("notify_url", order.getNotifyUrl());
        urlParams.put("trade_type", "NATIVE");

        try {
            // 发送请求，传递xml参数，微信支付提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // xml参数加密时，将sign签名注入
            client.setXmlParam(WXPayUtil.generateSignedXml(urlParams, order.getPartnerKey()));
            client.setHttps(true);
            client.post();
            // 使用xml返回结果
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            new RedirectAttributesModelMap().addAttribute("xml", xml);

            log.info("====================订单的xml解析地址===================");
            System.out.println(xml);

            HashMap<String, String> map = new HashMap<>();
            map.put("outTradeNo", order.getOutTradeNo());
            map.put("totalFee", String.valueOf(order.getTotalFee()));
            map.put("return_code", resultMap.get("return_code"));
            map.put("code_url", resultMap.get("code_url"));
            map.put("xml", xml);

            log.info("=======================返回数据解析===================");
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
