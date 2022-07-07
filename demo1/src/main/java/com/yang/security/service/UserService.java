package com.yang.security.service;

import com.yang.security.entity.User;
import com.yang.security.entity.dto.LoginDto;
import com.yang.security.utils.Result;

import java.util.List;

public interface UserService {
    List<User> findAll();

    Result loginByPassword(LoginDto loginDto);
}
