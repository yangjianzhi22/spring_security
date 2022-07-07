package com.yang.security.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.yang.security.Constants.RedisConstants;
import com.yang.security.entity.dto.UserDto;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 获取Token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            // 没有获取到Token 放行，让其它过滤器去拦截
            filterChain.doFilter(request, response);
            return;
        }
        // 2. 解析Token 获取userID
        String userId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Token异常!");
        }
        // 3. 从Redis中取出用户数据
        String redis_key = RedisConstants.LOGIN_USER_KEY + userId;
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(redis_key);
        if (map.isEmpty()) {
            throw new RuntimeException("Token异常!");
        }
        // TODO 从Redis中获取的是hash结构 需要转为实体
        UserDto userDto = BeanUtil.fillBeanWithMap(map, new UserDto(), false);

        //  TODO 4. 将用户信息存入SecurityContextHolder
        /**
         * setAuthentication()方法需要一个authentication对象，
         * 所以我们需要把用户信息封装到authentication中
         */
        // TODO 获取权限信息封装到 authentication 中(第三个参数)
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDto,null,null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 5. 放行
        filterChain.doFilter(request, response);
    }
}
