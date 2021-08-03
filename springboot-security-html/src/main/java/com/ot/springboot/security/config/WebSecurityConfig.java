package com.ot.springboot.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
//配置的实例上使用此注解开启基于注解的安全性
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //因为我们自己的登录页面是post提交（get不支持）所以会被csrf过滤器所拦截，我们没有携带token，因此不能登录成功，因此得禁用此过滤器
        //spring security为了防止csrf跨站请求伪造的发生，限制了除了get以外的大多数方法
        http
                .csrf()
                //使用cookie，存储在cookie当中只能在同域当中获取，所以杜绝第三方网站获取cookie的可能，cfrs攻击本身是不知道cookie内容的，只是
                //利用了请求携带cookie身份验证的漏洞，但服务器本身进行校验的时候，需要从请求参数或者请求头当中获取token与cookie当中比较，crfs
                //攻击确实可以在获取到cookie的值，但是请求参数里面没有，因此失败
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                //静态资源放行
                .antMatchers(HttpMethod.GET, "/login.html", "/login").permitAll()
                .antMatchers("/r/r1").hasAuthority("p1")
                .antMatchers("/r/r2").hasAuthority("p2")
                .antMatchers("/r/r3").access("hasAuthority('p1') and hasAuthority('p2')") //SPEL表达式
                .antMatchers("/r/**").authenticated()
                .anyRequest().permitAll()//除了/r/**，其它的请求可以访问

                .and()
                .formLogin()//允许表单登录,允许表单登录会创建usernamePasswordAuthenticationFilter，此时登录请求默认是/login，post
                .loginPage("/login.html")//用户未登录的时候，访问任何资源都跳转到该路径，即登录页面
                .loginProcessingUrl("/customerLogin")//即登录表单当中action的地址，处理认证请求的路径
                .usernameParameter("username1")//表单登录请求的username
                .passwordParameter("password1")//表单登录请求的password
                .defaultSuccessUrl("/login-success")//登录成功之后看到的
                .successForwardUrl("/login-success")//自定义登录成功的页面地址

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login.html");
    }
}
