package com.ot.springboot.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@Controller
public class LoginController {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @RequestMapping(value = "/login-success")
    @ResponseBody
    public String loginSuccess(HttpSession session) {
        AuthenticationManager object = authenticationManagerBuilder.getObject();
        System.out.println(object.getClass());
        System.out.println("登录成功：" + session.getId());
        String username = getUsername();
        return username + "登录成功";
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        System.out.println("首页：" + session.getId());
        return "redirect:/login.html";
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            UserDetails details = (UserDetails) principal;
            username = details.getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    /**
     * 测试资源1
     *
     * @return
     */
    @GetMapping(value = "/r/r1")
    @ResponseBody
    public String r1(HttpSession session) {
        System.out.println("访问资源1:" + session.getId());
        return " 访问资源1";
    }

    /**
     * 测试资源2
     *
     * @return
     */
    @GetMapping(value = "/r/r2")
    @ResponseBody
    public String r2() {
        return " 访问资源2";
    }

    /**
     * 测试资源3
     *
     * @return
     */
    @GetMapping(value = "/r/r3")
    @ResponseBody
    public String r3() {
        return " 访问资源3";
    }

    @GetMapping("/cookie")
    @ResponseBody
    public String cookieHttpOnly(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("a", "A");
        response.addCookie(cookie);
        return "success";
    }


    @GetMapping(value = "/test")
    @ResponseBody
    public String test() {
        return "不登陆就可以访问test";
    }

    /**
     * 注意 使用@Secured，而@Secured对应的角色必须要有ROLE_前缀。使用@PreAuthorize是可以随意设置的 这个也不可以随意设置，如果使用hasRole还是需要
     * 前缀
     *
     * @return
     */
    @GetMapping(value = "/test1")
    @ResponseBody
    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    public String test1() {
        return "登陆才可以访问test1";
    }

    @GetMapping(value = "/test11")
    @ResponseBody
    @Secured("p1")
    public String test11() {
        return "登陆才可以访问test11";
    }

    @GetMapping(value = "/test2")
    @ResponseBody
    @PreAuthorize("hasAuthority('p1')")
    public String test2() {
        return "登陆才可以访问test2";
    }

    //前后端分离登录请求编写
//    @GetMapping(value = "/test3")
//    @ResponseBody
//    @Secured("ROLE_ADMIN")
//    public String test3() {
//        return "不登陆就可以访问";
//    }
//    @ApiOperation("登录授权")
//    @AnonymousPostMapping(value = "/login")
//    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
//        // 密码解密
//        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
//        // 查询验证码
//        String code = (String) redisUtils.get(authUser.getUuid());
//        // 清除验证码
//        redisUtils.del(authUser.getUuid());
//        if (StringUtils.isBlank(code)) {
//            throw new BadRequestException("验证码不存在或已过期");
//        }
//        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
//            throw new BadRequestException("验证码错误");
//        }
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
//        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        // 生成令牌
//        String token = tokenProvider.createToken(authentication);
//        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
//        // 保存在线信息
//        onlineUserService.save(jwtUserDto, token, request);
//        // 返回 token 与 用户信息
//        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
//            put("token", properties.getTokenStartWith() + token);
//            put("user", jwtUserDto);
//        }};
//        if (loginProperties.isSingleLogin()) {
//            //踢掉之前已经登录的token
//            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
//        }
//        return ResponseEntity.ok(authInfo);
//    }
}
