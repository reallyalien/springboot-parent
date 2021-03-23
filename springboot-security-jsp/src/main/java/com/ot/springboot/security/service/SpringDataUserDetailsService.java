package com.ot.springboot.security.service;

import com.ot.springboot.security.dao.UserDao;
import com.ot.springboot.security.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SpringDataUserDetailsService implements UserDetailsService {


    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("登录用户名：" + username);
        //从数据库查询
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            return null;
        }
        //查询用户权限
        List<String> permissions = userDao.findPermissionsByUserId(user.getId());
        //spring_security需要的需要同认证信息对比的userDatails
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()).authorities(permissions.toArray(new String[]{}))
                .build();
        return userDetails;
    }
}
