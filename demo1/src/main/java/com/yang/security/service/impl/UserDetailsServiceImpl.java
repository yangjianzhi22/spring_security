package com.yang.security.service.impl;

import com.yang.security.entity.User;
import com.yang.security.mapper.UserMapper;
import com.yang.security.service.LoginUser;
import com.yang.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        User user = userMapper.findUserByUsername(username);

        /**
         * 若查询不到用户信息，则抛异常
         * SpringSecurity可以处理我们在查询中遇到的异常
         */
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // TODO 根据用户查询权限信息，添加到LoginUser中

        // 因为返回值是UserDetails，所有需要定义一个类，实现UserDetails，把用户信息封装在其中
        return new LoginUser(user);
    }
}
