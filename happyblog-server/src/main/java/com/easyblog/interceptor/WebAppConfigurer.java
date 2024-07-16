package com.happyblog.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration("webAppConfigurer")
public class WebAppConfigurer implements WebMvcConfigurer {

    @Resource
    private AppInterceptor appInterceptor;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * addPathPatterns 用于添加拦截规则
         * excludePathPatterns 用于排除拦截
         */
        registry.addInterceptor(appInterceptor).addPathPatterns("/**");
        // .excludePathPatterns("/hello/getAll");
    }

    /**
     * 配置静态资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 配置不拦截静态资源
         */
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}