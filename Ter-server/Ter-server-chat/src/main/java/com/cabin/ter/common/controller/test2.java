package com.cabin.ter.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test2 {
    @GetMapping("/test2")
    public String test2(){
        return "测试2";
    }
}
