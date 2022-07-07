package com.yang.security.mapper;

import com.yang.security.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> findAll();

    User findUserByUsername(@Param("username" )String username);
}
