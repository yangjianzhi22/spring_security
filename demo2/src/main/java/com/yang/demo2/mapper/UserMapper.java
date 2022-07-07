package com.yang.demo2.mapper;

import com.yang.demo2.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    User queryByUsername(@Param("username") String username);

    int add(User user);
}