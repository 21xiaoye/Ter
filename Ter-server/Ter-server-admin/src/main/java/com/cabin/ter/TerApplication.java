package com.cabin.ter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.cabin.ter",scanBasePackageClasses = TerApplication.class)
@ServletComponentScan
@MapperScan({"com.cabin.ter.**.mapper"})
@ComponentScan("com.cabin.ter.**")
@EnableAsync
public class TerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TerApplication.class, args);
        System.out.println("启动成功了...");
    }
}
