package com.cabin.ter.common.controller;


import com.cabin.ter.admin.domain.User;
import com.cabin.ter.admin.mapper.PermissionMapper;
import com.cabin.ter.admin.mapper.UserMapper;
import com.cabin.ter.config.SaltConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@RequestMapping("/test")
public class hello{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SaltConfig saltConfig;
    @Autowired
    private PermissionMapper permissionMapper;

    @GetMapping("/hello")
    public String hello() {
        Long id = 1072806377661009920L;
        User userId = userMapper.getUserId(id);
        System.out.println(userId);
        String passwd ="Thbbrxrzy666";
        SaltConfig saltConfig1 = saltConfig.passwdEncryption(passwd);
        System.out.println(saltConfig1);
        String salt = saltConfig1.getSalt();
        SaltConfig saltConfig2 = saltConfig.passwdDecryption(passwd, salt);
        System.out.println(saltConfig2);


        return "hello world";
    }


}
