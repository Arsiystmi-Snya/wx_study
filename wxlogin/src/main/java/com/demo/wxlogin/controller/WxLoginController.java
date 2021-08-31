package com.demo.wxlogin.controller;

import com.demo.wxlogin.utils.WxUtils;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @Package: com.demo.wxlogin.controller
 * @author: jt
 * @date: 2021-08-31   09:19
 * @Description:
 */
@Controller
@RequestMapping("/wx")
public class WxLoginController {

    /**
     * 获取微信登录二维码
     * @return 扫码登录界面
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/login")
    public String createQRcode() throws UnsupportedEncodingException {
        // 先对回调地址做url编码
        String redirectUrl = URLEncoder.encode(WxUtils.wx_open_redirect_url, "utf8");

        String url = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=" + WxUtils.wx_open_app_id + // appid
                "&redirect_uri=" + redirectUrl + // 回调url
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=" + "微信登录demo" +
                "#wechat_redirect";

        return "redirect:" + url;
    }

    /**
     * 获取 微信userInfo
     * @param code
     * @param state
     */
    @RequestMapping("/访问地址")
    public void callBack(String code, String state) {
        // code，类似于微信官方生成的token，在一定时间内有效
        System.out.println("打印:" + code + "------------------" + state);

        // 通过code，去请求微信提供的地址
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=" + WxUtils.wx_open_app_id +
                "&secret=" + WxUtils.wx_open_app_secret +
                "&code=" + code +
                "&grant_type=authorization_code";

        // 发送get请求
        String openidInfo = sendGet(url);

        // 使用Gson转换json，获取token和openid
        Gson gson = new Gson();
        HashMap mapAccessToken = gson.fromJson(openidInfo, HashMap.class);
        String accessToken = (String) mapAccessToken.get("access_token");
        String openid = (String) mapAccessToken.get("openid");

        // 通过token和openid，获取用户信息
        String userInfoUrl = "https://api.wexin.qq.com/sns/userinfo" +
                "?access_token=" + accessToken +
                "&openid=" + openid;

        String userInfo = sendGet(userInfoUrl);

        // 解码json
        HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
        String nickname = (String) userInfoMap.get("nickname");
        String icon = (String) userInfoMap.get("headimgurl");

        System.out.println(nickname + "---" + icon);

        // 将用户数据存储到数据库
    }

    /**
     * 模拟get请求
     * @param getUrl url参数地址
     * @return
     */
    private String sendGet(String getUrl) {
        try {
            // 转换成url地址
            URL url = new URL(getUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // 获取输入流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf8"));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            String s = bufferedReader.readLine();
            // 循环读取流
            while ((line = s) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();

            connection.disconnect();

            System.out.println(stringBuilder.toString());
            return stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("url格式错误");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "获取成功";
    }
}
