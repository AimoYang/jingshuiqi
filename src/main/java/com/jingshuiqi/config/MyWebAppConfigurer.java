package com.jingshuiqi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//配置允许跨域请求
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter{
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedHeaders("*")
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
        .allowCredentials(true)
        .maxAge(3600);
    }
}
