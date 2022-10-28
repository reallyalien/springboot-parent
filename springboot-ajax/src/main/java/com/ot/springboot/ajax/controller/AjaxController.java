package com.ot.springboot.ajax.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.ot.springboot.ajax.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/ajax")
public class AjaxController {


    private RestTemplate restTemplate = new RestTemplate();
    private static Map<String, User> maps = new ConcurrentHashMap<>();

    @GetMapping("/hello")
    public String hello(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("my-cookie", "cookie-value");
        //在网页端无法获取通过js脚本获取cookie，避免xss攻击，只能在服务端获取cookie
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        System.out.println("hello");
        return "admin";
    }

    @GetMapping("/hello1")
    public String hello1(HttpServletRequest request, HttpServletResponse response) {
        return "admin1";
    }

    @PostMapping("/arr")
    public void hello(@RequestBody User user) throws IOException {
        FileOutputStream fos = new FileOutputStream("d:/1.txt");
        fos.write(user.getArr());
        fos.close();
    }

    @GetMapping("/cookie")
    public String cookieHttpOnly(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("a", "A");
//        Session.Cookie cookie=new Session.Cookie();
        cookie.setPath("/aaaa");
        cookie.setDomain("/aa");
        return "success";
    }

    @PostMapping("/json")
    public void hello(HttpServletRequest request) throws IOException {
        User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        System.out.println(user);
    }

    @PostMapping("/accept")
    public void accept(@RequestBody User user) throws IOException {
        maps.put("1", user);
        System.out.println(user);
    }

    @GetMapping("/findUser")
    public User findUser() throws IOException {
        User user = maps.get("1");
        System.out.println(user);
        return user;
    }

    @PostMapping("/formData")
    public void formData(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        file.transferTo(new File("/1.txt"));
        byte[] bytes = file.getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);
    }

    @GetMapping("/aaa")
    public void f(Date date, Long id) {
        System.out.println(date);
        System.out.println(id);
    }

    @GetMapping("/testGet")
    public List<User> testGet() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setName("a" + i);
            list.add(user);
        }
        return list;
    }

    @PostMapping("/testPost")
    public User testPost(@RequestBody User user) {
        return user;
    }


    @GetMapping("/isout")
    public void isOutInpatient(String inpatientNo, Map<String, Object> param) {
        try {
            MultiValueMap<String, String> header = new HttpHeaders();
            header.add("X-Api-Key", "gc8U4S37ZhhoQZNeZZ0CfcWgIBfkgFEEPIWvpJaBY6DQiWWh9seq/2EckpsvVJ5saoB0PfAMEuWKrf+LGcfHkvMTcHaDc6mr7BpR+PHJF9WkZEqdMTwIY8X4O5kZiWm3qGbpjnZcwwS1ljQRuOFtSg==");
            header.add("X-Api-Ver", "2");
            Map<String, Object> body = new HashMap<>();
            body.put("1", "2");
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, header);
            ResponseEntity<Object> exchange = restTemplate.exchange("http://127.0.0.1:8080/sync/v1/inpa/outhos/check?inpatientNo=0021001515", HttpMethod.POST, httpEntity, Object.class);
            System.out.println(exchange);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void a() throws InstantiationException, IllegalAccessException {
        List b = b(List.class);
    }

    public <T> T b(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T o = clazz.newInstance();
        return o;
    }
}
