package com.aitool.config.satoken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 10100
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细的拦截路由
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/logout",
                        "/auth/verify",
                        // knife4j接口文档
                        "/swagger-ui/**",
                        // knife4j相关资源
                        "/webjars/**",
                        // openapi接口文档
                        "/v3/api-docs/**",
                        // openapi接口文档
                        "/doc.html",
                        "/favicon.ico",
                        "/swagger-resources",
                        "/api/**",
                        "/wechat/**",
                        "/localFile/**"
                );
    }
}