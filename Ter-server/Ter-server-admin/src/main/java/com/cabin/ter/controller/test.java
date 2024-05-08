package com.cabin.ter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @GetMapping("/test1")
    public String test1(){
        return "测试";
    }
}
