package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.example.utils.MySnowFlakeGenerator"); // 初始化雪花机器位
        SpringApplication.run(DemoApplication.class, args);
    }
}
