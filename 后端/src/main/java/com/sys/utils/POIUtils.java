package com.sys.utils;

import com.sys.entity.ExcelUser;
import com.sys.entity.User;
import com.sys.exception.AppException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author wp
 * @date 2018/4/27 10:17
 */
public class POIUtils {

    public static List<ExcelUser> importExcelFile(MultipartFile file, String fileType) {

        List<ExcelUser> users = new ArrayList<>();
        Workbook workbook = null;
        try {

            if ("xls".equals(fileType)) {

                workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            } else {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                ExcelUser excelUser;
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        continue;//没数据
                    }
                    int physicalNumberOfCells = 6; //设成死的原因是 当其中一个单元格为空时，自动解析会移除导致程序出错
                    excelUser = new ExcelUser();
                    for (int k = 0; k < physicalNumberOfCells; k++) {
                        Cell cell = row.getCell(k);
                        if (cell == null) {
                            continue;
                        }
                        switch (cell.getCellTypeEnum()) {
                            case NUMERIC:
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                break;
                            default:
                        }
                        String cellValue = cell.getStringCellValue().trim();
                        switch (k) {
                            case 0:
                                excelUser.setAccount(cellValue);
                                break;
                            case 1:
                                excelUser.setName(cellValue);
                                break;
                            case 2:
                                excelUser.setCollege(cellValue);
                                break;
                            case 3:
                                excelUser.setProfession(cellValue);
                                break;
                            case 4:
                                excelUser.setClassroom(cellValue);
                                break;
                            case 5:
                                excelUser.setLevel(LevelUtils.transLevel(cellValue));
                                break;
                            default:

                        }

                    }
                    users.add(excelUser);
                }
            }

        } catch (Exception e) {
            throw new AppException(StatusEnum.SERVER_ERROR.getCode(), e.getMessage());
        }

        return users;
    }

    public static Workbook exportExcelFile(List<User> data) {

        String fileName = "sheet1";
        Map<String, String> headMap = new LinkedHashMap<>(5);
        headMap.put("account", "学号");
        headMap.put("name", "姓名");
        headMap.put("college", "学院");
        headMap.put("profession", "专业");
        headMap.put("classroom", "班级");
        headMap.put("score", "学分");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(fileName);

        int rowIndex = 0, columnIndex = 0;
        Set<String> keys = headMap.keySet();

        //表头
        Row row = sheet.createRow(rowIndex++);
        for (String key : keys) {
            Cell cell = row.createCell(columnIndex++);
            cell.setCellValue(headMap.get(key));
        }

        //内容
        if (data != null && !data.isEmpty()) {
            Map map;
            for (User user : data) {
                row = sheet.createRow(rowIndex++);
                columnIndex = 0;
                map = BeanUtils.toMap(user);
                for (String key : keys) {
                    Cell cell = row.createCell(columnIndex++);
                    setCellValue(cell, map.get(key));
                }
            }
        }

        return workbook;

    }

    private static void setCellValue(Cell cell, Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof String) {
            cell.setCellValue((String) obj);
        } else if (obj instanceof Double) {
            cell.setCellValue((Double) obj);
        } else {
            cell.setCellValue(obj.toString());
        }
    }

}
