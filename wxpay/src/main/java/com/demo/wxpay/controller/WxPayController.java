package com.demo.wxpay.controller;

import com.demo.wxpay.pojo.HttpClient;
import com.demo.wxpay.utils.WxPayUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package: com.demo.wxpay.controller
 * @author: jt
 * @date: 2021-08-31   15:54
 * @Description:
 */
@Controller
@RequestMapping("/wx")
public class WxPayController {

    /**
     * 生成支付二维码
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/pay")
    public String createPayQRcode(Model model) throws Exception {
        // 价格，应该从数据库中获取
        String price = "0.01";

        // 订单号
        String orderNo = WxPayUtils.createOrderNo();

        HashMap<String, String> orderInfo = new HashMap<>();
        orderInfo.put("appid", WxPayUtils.wx_pay_appId);
        orderInfo.put("mch_id", WxPayUtils.wx_pay_partner);
        // 随机字符串，保证签名不可预测
        orderInfo.put("nonce_str", WXPayUtil.generateNonceStr());
        orderInfo.put("body", "微信支付测试demo");
        orderInfo.put("out_trade_no", orderNo);
        // 项目的域名，终端ip
        orderInfo.put("spbill_create_ip", "127.0.0.1");
        orderInfo.put("notify_url", WxPayUtils.wx_pay_notifyUrl);
        // 支付类型，扫码支付
        orderInfo.put("trade_type", "NATIVE");

        // 发送请求，传递xml参数，微信支付提供的固定地址
        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        // xml参数加密
        client.setXmlParam(WXPayUtil.generateSignedXml(orderInfo, WxPayUtils.wx_pay_partnerKey));
        client.setHttps(true);

        // post 请求发送
        client.post();

        // 使用xml返回结果
        String xml = client.getContent();
        Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

        HashMap<String, String> map = new HashMap<>();
        map.put("orderNo", orderNo);
        map.put("price", price);
        map.put("result_code", resultMap.get("result_code"));
        map.put("code_url", resultMap.get("code_url"));

        model.addAttribute("map", map);
        return "pay";
    }

    /**
     * 判断支付是否完成
     * @param no
     * @return
     * @throws Exception
     */
    @GetMapping("/queryOrder/{no}")
    public String queryPayStatus(@PathVariable("no") String no) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("appid", WxPayUtils.wx_pay_appId);
        params.put("mch_id", WxPayUtils.wx_pay_partner);
        params.put("out_trade_no", no);
        params.put("nonce_str", WXPayUtil.generateNonceStr());

        // 发送httpclient
        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        client.setXmlParam(WXPayUtil.generateSignedXml(params, WxPayUtils.wx_pay_partnerKey));
        client.setHttps(true);
        client.post();

        // 得到订单数据
        String xml = client.getContent();
        Map<String, String> resutlMap = WXPayUtil.xmlToMap(xml);

        // 判断是否支付成功
        String status = resutlMap.get("trade_state");
        if ("SUCCESS".equals(status)) {
            /*
                改变数据库中的数据等操作
             */
            return "支付成功";
        }
        return "支付中";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

}
