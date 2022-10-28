package org.example.excel.controller;

import org.example.excel.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;


    @GetMapping("/import")
    public void importExcel(@RequestParam(required = true) String filePath) {
        excelService.importExcel(filePath);
    }
}
