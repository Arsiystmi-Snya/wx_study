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
        registry.addViewController("/order.ftl").setViewName("order");
    }
}
