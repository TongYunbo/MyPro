package com.tzcpa.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：zc
 * @date ：Created in 2019/3/16 17:36
 * @description：
 */
@Configuration
public class MyAdapter implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(MyAdapter.class);

    //支持跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("===== 支持跨域请求 =====");
        registry.addMapping("/**")
                .allowedHeaders("Content-Type", "X-Requested-With", "accept,Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "token")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowCredentials(true);

    }

    // 登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("===== 登录拦截器 =====");
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("classpath:/static/**","/static/**","/login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
