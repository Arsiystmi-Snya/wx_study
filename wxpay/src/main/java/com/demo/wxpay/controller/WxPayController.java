package com.demo.wxpay.controller;

import com.demo.wxpay.service.WxPayService;
import com.demo.wxpay.utils.HttpClient;
import com.demo.wxpay.utils.WxPayUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    WxPayService wxPayService;

    /**
     * 生成支付二维码
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/pay")
    public String createPayQRcode(Model model) throws Exception {
        // 价格，应该从数据库中获取
        Integer totalFee = 1;

        // 订单号
        String outTradeNo = WxPayUtils.createorderidByuuid();

        Map<String, String> map = wxPayService.createPayQRcode(totalFee, outTradeNo);

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
