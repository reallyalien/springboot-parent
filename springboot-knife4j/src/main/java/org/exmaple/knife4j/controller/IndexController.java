package org.exmaple.knife4j.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.exmaple.knife4j.config.FtpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "首页模块")
@RestController
public class IndexController {


    @Autowired
    private FtpConfig config;

    @Value("#{${my_map}}")
    private Map<String, String> my_map;

    @Parameter(name = "name", description = "姓名", required = true)
    @Operation(summary = "向客人问好")
    @GetMapping("/sayHi")
    public ResponseEntity<String> sayHi(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok("Hi:" + name);
    }
}