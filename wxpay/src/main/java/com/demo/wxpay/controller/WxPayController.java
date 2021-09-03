package com.demo.wxpay.controller;

import com.demo.wxpay.pojo.Order;
import com.demo.wxpay.pojo.Result;
import com.demo.wxpay.service.WxPayService;
import com.demo.wxpay.utils.WxPayUtils;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
@Slf4j
public class WxPayController {
    private Map<String, SseEmitter> sseEmitters = new HashMap<>();

    @Autowired
    WxPayService wxPayService;

    /**
     * 生成微信的callBackUrl
     * @param order
     * @return
     */
    @PostMapping("/pay")
    @ResponseBody
    public Result createPayQrCode(Order order) {
        order.setOutTradeNo(WxPayUtils.createorderidByuuid());

        try {
            String callBcakUrl = wxPayService.createPayQrCode(order);

            return new Result(callBcakUrl, "生成订单成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(null, "生成订单失败", false);
        }
    }

    /**
     * 生成支付二维码
     * @return
     * @throws Exception
     */
    @RequestMapping("/pays")
    public String createPayQRcode(HttpServletRequest request) throws Exception {
        // 价格，应该从数据库中获取
        Integer totalFee = 1;

        // 订单号
        String outTradeNo = WxPayUtils.createorderidByuuid();

        Map<String, String> map = wxPayService.createPayQRcode(totalFee, outTradeNo);

        request.setAttribute("map", map);

        Object map1 = request.getAttribute("map");
        return "pay";
    }

    /**
     * 判断支付是否完成
     * @param outTradeNo
     * @return
     * @throws Exception
     */
    @GetMapping("/queryOrder/{no}")
    public String queryPayStatus(@PathVariable("no") String outTradeNo) throws Exception {
        // TODO 异步通知商户支付结果，告知支付情况
        Map<String, String> map = wxPayService.queryOrderStatus(outTradeNo);

        // 判断支付是否成功
        if (map.get("trade_state").equals("SUCCESS")) {
            // 进行改变数据库中的操作
            return "success";
        }
        return "false";
    }

    @GetMapping("/test")
    public String demoTest(Model model) {
        model.addAttribute("flag", true);
        return "success";
    }

    /*
        SSE 必须返回SseEmitter对象，必须必须返回SseEmitter对象，SseEmitter对象是Session级别的，
        如果你要点对点针对每个session要独立存储。如果你是广播可以公用一个SseEmitter对象。
        按照SSE规范也必须声明produces为"text/event-stream"。当你调用该接口的时候将建立起SSE连接。
        你可以在另一个线程中调用SseEmitter的send方法向客户端发送事件。你也可以在发送事件后调用complete方法来关闭SSE连接
     */

    /**
     * SSE
     * @param id
     * @return sseEmitter
     * 访问此连接，来完成单向通讯
     */
    @GetMapping(path = "/send/{id}", produces = "text/event-stream")
    public SseEmitter sendMessage(@PathVariable("id") String id) {
        SseEmitter sseEmitter = new SseEmitter();
        this.sseEmitters.put(id, sseEmitter);
        return sseEmitter;
    }

    @ResponseBody
    @RequestMapping(value = "/getdata", produces = "text/event-stream;charset=UTF-8")
    public String push() {
        try {
            Thread.sleep(1000);
            // 第三方数据源调用
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double random = Math.random();

        return String.valueOf(random);
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 功能描述：微信回调通知
     * @param request 请求
     * @return response 响应
     */
    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("\n callback/notify 被调用");

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        // sb为微信返回的xml
        String notifyXml = sb.toString();

        // ServletInputStream inputStream = request.getInputStream();
        // String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");
        log.info("\n notifyXml = \n " + notifyXml);

        // todo 获取微信的回调xml

        // 秘钥验签：验证签名是否正确
        if (WXPayUtil.isSignatureValid(notifyXml, WxPayUtils.wx_pay_partnerKey)) {

            // 解析返回结果
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);

            // 判断支付是否成功
            // result_code 判断支付是否交易成功
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                // 金额校验
                String totalFee = notifyMap.get("total_fee");
                String outTradeNo = notifyMap.get("out_trade_no");
                // todo 业务逻辑判断
                //查询本地订单
                // 校验返回的订单金额是否与商户侧的订单金额一致
                // 接口调用的幂等性：无论接口被调用多少次，最后所影响的结果都是一致的
                // 更新订单状态

                // 支付成功：给微信发送我已接收通知的响应
                // 创建响应对象
                Map<String, String> returnMap = new HashMap<>();
                returnMap.put("return_code", "SUCCESS");
                returnMap.put("return_msg", "OK");
                String returnXml = WXPayUtil.mapToXml(returnMap);
                response.setContentType("text/xml");
                log.info("支付成功，通知已处理");
                return returnXml;
            }
        }

        // 创建响应对象：微信接收到校验失败的结果后，会反复的调用当前回调函数
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code", "FAIL");
        returnMap.put("return_msg", "");
        String returnXml = WXPayUtil.mapToXml(returnMap);
        response.setContentType("text/xml");
        log.info("校验失败");
        return returnXml;
    }
}
