package com.sys.config.interceptor;

import com.sys.config.controller.AuthHandle;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration ir = registry.addInterceptor(new AuthHandle());
        ir.addPathPatterns("/**");
    }
}
