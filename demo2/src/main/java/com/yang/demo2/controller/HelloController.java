package com.yang.demo2.controller;

import com.yang.demo2.entity.User;
import com.yang.demo2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/logout/success")
    public Object logout() {
        return "退出成功，请重新登录!";
    }

    @GetMapping("/login")
    public Object login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        }catch (Exception e) {
            return "用户名密码不正确，请重新登陆!";
        }
    }

    @GetMapping("/get-user")
    public Object getUser(@RequestParam String username) {
        return userService.queryByUsername(username);
    }

    @GetMapping("/hi")
    public Object getUser() {
        return "hello world";
    }

    @PostMapping("/add-user")
    public int addUser(@RequestBody User user){
        return userService.add(user);
    }
}