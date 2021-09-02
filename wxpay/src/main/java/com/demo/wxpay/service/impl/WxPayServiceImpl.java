package com.demo.wxpay.service.impl;

import com.demo.wxpay.service.WxPayService;
import com.demo.wxpay.utils.HttpClient;
import com.demo.wxpay.utils.WxPayUtils;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            String resultXml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

            log.info("====================订单的xml解析地址===================");
            System.out.println(resultXml);

            HashMap<String, String> map = new HashMap<>();
            map.put("outTradeNo", outTradeNo);
            map.put("totalFee", String.valueOf(totalFee));
            map.put("return_code", resultMap.get("return_code"));
            map.put("code_url", resultMap.get("code_url"));
            log.info("=======================返回数据解析===================");
            System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
