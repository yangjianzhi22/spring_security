package com.yang.demo2.service;

import com.yang.demo2.entity.User;
import com.yang.demo2.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public User queryByUsername(String username) {
        return userMapper.queryByUsername(username);
    }

    public int add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.add(user);
    }
}
