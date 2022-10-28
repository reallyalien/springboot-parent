package org.example.excel.util;


import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;


/**
 * Excel相关处理
 *
 * @author admin
 */
@Component
public class ExcelUtil<T> {


    @Autowired
    private DatasourceUtil datasourceUtil;

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @return 转换后集合
     */
    public void importExcel(String filePath) throws Exception {
        FileInputStream is = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(is);
        List<Sheet> sheets = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            sheets.add(wb.getSheetAt(i));
        }
        for (Sheet sheet : sheets) {
            String sheetName = sheet.getSheetName();
            int rows = sheet.getLastRowNum();
            if (rows > 0) {
                // 定义一个map用于存放excel列的序号和field.
                List<String> columns = new ArrayList<>();
                // 获取表头
                Row heard = sheet.getRow(0);
                for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++) {
                    Cell cell = heard.getCell(i);
                    if (null != cell) {
                        String value = this.getCellValue(heard, i).toString();
                        columns.add(value);
                    }
                }
                //读取数据
                List<List<Object>> result = new ArrayList<>();
                int[] len = new int[columns.size()];
                for (int i = 1; i <= rows; i++) {
                    Row row = sheet.getRow(i);
                    List<Object> sArr = new ArrayList<>(columns.size());
                    for (int j = 0; j < heard.getPhysicalNumberOfCells(); j++) {
                        Cell cell = row.getCell(j);
                        if (null != cell) {
                            String value = this.getCellValue(row, j).toString();
                            sArr.add(value);
                            if (len[j] < value.length()) {
                                len[j] = value.length();
                            }
                        } else {
                            sArr.add("");
                        }
                    }
                    result.add(sArr);
                }
                datasourceUtil.createTableAndInsert(sheetName, columns, len, result);
            }
        }
    }

    /**
     * 获取单元格值
     *
     * @param row    获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column) {
        if (row == null) {
            return row;
        }
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (null != cell) {
                if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                    val = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell)) {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                    } else {
                        if ((Double) val % 1 != 0) {
                            val = new BigDecimal(val.toString());
                        } else {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                } else if (cell.getCellType() == CellType.STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == CellType.ERROR) {
                    val = cell.getErrorCellValue();
                }

            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }
}
