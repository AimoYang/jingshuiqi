package com.jingshuiqi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@SpringBootApplication
@EnableSwagger2 //启动Swagger2
@MapperScan(basePackages = "com.jingshuiqi.dao") //扫描dao�?
public class Application {
		
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
	}
}
