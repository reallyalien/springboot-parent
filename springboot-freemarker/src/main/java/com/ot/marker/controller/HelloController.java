package com.ot.marker.controller;

import com.ot.marker.utils.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private Resources resources;

    @GetMapping("/index")
    public String index(ModelMap map){
        map.addAttribute("resource",resources);
        return "index";
    }
}
