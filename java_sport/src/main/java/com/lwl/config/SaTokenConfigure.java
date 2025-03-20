package com.lwl.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有地址
        registry.addInterceptor(new SaInterceptor( handle -> {
                    SaRouter.match("/**")
                            //white list
                            .notMatch("/favicon.ico", "/doc.html", "/webjars/**", "/v3/api-docs/**")
                            .check(StpUtil::checkLogin);
                }))
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }
}
