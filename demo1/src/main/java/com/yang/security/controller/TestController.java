package com.yang.security.controller;

import com.yang.security.entity.dto.LoginDto;
import com.yang.security.service.UserService;
import com.yang.security.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public Object hello() {
        return userService.findAll();
    }

    @GetMapping("/hello3")
    public Object hello3() {
        return userService.findAll();
    }

    @GetMapping("/hello2")
    public Object loginByPassword() {
        userService.loginByPassword(new LoginDto("root", "237502", "user"));
        return userService.findAll();
    }
}