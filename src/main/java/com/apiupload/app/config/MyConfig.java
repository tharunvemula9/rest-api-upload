package com.apiupload.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.apiupload.restapi.interceptors.ApiUploadInterceptor;



@Configuration
public class MyConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new ApiUploadInterceptor()).addPathPatterns("/**");
    }
}
