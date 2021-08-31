package com.demo.wxlogin.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Package: com.demo.wxlogin.config
 * @author: jt
 * @date: 2021-08-31   09:27
 * @Description:
 */
@Component
public class WxUtils implements InitializingBean {

    @Value("${wx.open.app_id}")
    private String appId;

    @Value("${wx.open.app_secret}")
    private String appSecret;

    @Value("${wx.open.redirect_url}")
    private String url;

    public static String wx_open_app_id;
    public static String wx_open_app_secret;
    public static String wx_open_redirect_url;

    @Override
    public void afterPropertiesSet() {
        wx_open_app_id = appId;
        wx_open_app_secret = appSecret;
        wx_open_redirect_url = url;
    }
}
