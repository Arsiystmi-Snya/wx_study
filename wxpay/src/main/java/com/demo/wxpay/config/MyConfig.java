package com.demo.wxpay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Package: com.demo.wxpay.config
 * @author: jt
 * @date: 2021-09-03   18:32
 * @Description: mvcConfig配置
 */
@Configuration
public class MyConfig implements WebMvcConfigurer {
    /**
     * 自定义视图跳转
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 通过 order.html 访问，仍然跳转至 order.html 界面
        // 注意SetViewName方法就是访问的动态界面名，所以不添加后缀
        registry.addViewController("/order.html").setViewName("order");
        registry.addViewController("/pay.html").setViewName("pay.html");
    }
}
