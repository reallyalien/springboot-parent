package org.example.excel.service;

import org.example.excel.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

    @Autowired
    private ExcelUtil excelUtil;

    public void importExcel(String filePath) {
        try {
            excelUtil.importExcel(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
