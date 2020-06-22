package com.ot.websocket.controller;

import com.ot.websocket.server.WebSocketServer;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class DemoController {

    public static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        DemoController.applicationContext = applicationContext;
    }

    @GetMapping("/index")
    @ResponseBody
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("请求成功");
    }

    @GetMapping("/page")
    public ModelAndView page() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("websocket");
        return mv;
    }

    @RequestMapping("/jsp")
    public String jsp() {
        return "websocket";
    }

    @RequestMapping("/push/{toUserId}")
    @ResponseBody
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message, toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

}
