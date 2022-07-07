package com.yang.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yang.security.Constants.RedisConstants;
import com.yang.security.entity.User;
import com.yang.security.entity.dto.LoginDto;
import com.yang.security.entity.dto.UserDto;
import com.yang.security.mapper.UserMapper;
import com.yang.security.service.LoginUser;
import com.yang.security.service.UserService;
import com.yang.security.utils.JwtUtil;
import com.yang.security.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public Result loginByPassword(LoginDto loginDto) {
        // AuthenticationManager 进行用户认证
        // 将用户登录的username和password封装成 Authentication 对象
        // 随后使用 authenticationManager 帮助我们完成认证操作
        // 而 authenticationManager 最终调用 authenticate() 方法完成认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 如果认证没通过 则给出相应提示
        if (authentication == null) {
            throw new RuntimeException("用户名或密码错误！");
        }

        // 认证通过，使用 userId 生成一个 Jwt,Jwt存入Result返回
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String user_id = loginUser.getUser().getId().toString();
        User user = loginUser.getUser();
        String jwtToken = JwtUtil.createJWT(user_id);

        // TODO 使用 StringRedisTemplate存储实体类型，用hash 不过需将实体转为Map
        UserDto userDto_redis = BeanUtil.copyProperties(user, UserDto.class);
        // TODO 由于 StringRedisTemplate要求所有字段都是String类型，而UserDto的id是long类型
        // TODO 所以需要把所有字段都修改为String类型
        Map<String, Object> map = BeanUtil.beanToMap(userDto_redis, new HashMap<>(), CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        // 把用户信息存储到Redis中 userid作为Key的前缀
        String login_key = RedisConstants.LOGIN_USER_KEY + user_id;
        stringRedisTemplate.opsForHash().putAll(login_key, map);

        // TODO 5.4 设置有效期
        stringRedisTemplate.expire(login_key, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);

        Map<String, String > hashMap = new HashMap<>();
        hashMap.put("authorization", jwtToken);
        return Result.ok(hashMap);
    }
}
