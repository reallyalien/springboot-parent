package com.ot.session.service.impl;

import com.ot.session.model.AuthenticationRequest;
import com.ot.session.model.UserDto;
import com.ot.session.service.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public UserDto authentication(AuthenticationRequest request) {
        if (request == null || StringUtils.isEmpty(request.getUsername()) || StringUtils.isEmpty(request.getPassword())) {
            throw new RuntimeException("账户或密码为空");
        }
        UserDto userDto = getUserDto(request.getUsername());
        if (userDto == null) {
            throw new RuntimeException("没有查询到该用户");
        }
        if (!Objects.equals(request.getPassword(), userDto.getPassword())) {
            throw new RuntimeException("账户或密码错误");
        }
        return userDto;
    }


    //模拟用户查询
    public UserDto getUserDto(String username) {
        return userMap.get(username);
    }

    //用户信息
    private Map<String, UserDto> userMap = new HashMap<>();

    {

        Set<String> authorities1 = new HashSet<>();
        authorities1.add("p1");
        Set<String> authorities2 = new HashSet<>();
        authorities2.add("p2");
        userMap.put("zhangsan", new UserDto("1010", "zhangsan", "123", "张三", "133443", authorities1));
        userMap.put("lisi", new UserDto("1011", "lisi", "456", "李四", "144553", authorities2));
    }
}
