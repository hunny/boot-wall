package com.example.bootwall.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author huzexiong
 */
@Component
public class ExcelReaderService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public void read(String path, SheetHandler handler) throws Exception {
    FileInputStream fis = new FileInputStream(new File(path));
    XSSFWorkbook workbook = new XSSFWorkbook(fis);
    if (null != handler) {
      int num = workbook.getNumberOfSheets();
      for (int index = 0; index < num; index++) {
        handleSheetAt(workbook, index, handler);
      }
    }
    workbook.close();
    fis.close();
  }

  private void handleSheetAt(XSSFWorkbook workbook, //
      int index, //
      SheetHandler handler) throws ParseException {
    XSSFSheet spreadsheet = workbook.getSheetAt(index);
    String sheetName = spreadsheet.getSheetName();
    Iterator<Row> rowIterator = spreadsheet.iterator();
    logger.debug("Ready for loading data from Excel of sheet [{}].", sheetName);
    int rowIndex = -1;
    while (rowIterator.hasNext()) {
      rowIndex++;
      XSSFRow row = (XSSFRow) rowIterator.next();
      Iterator<Cell> cellIterator = row.cellIterator();
      rowValues(sheetName, rowIndex, cellIterator, handler);
    }
  }

  private void rowValues(final String sheetName, //
      final int rowIndex, //
      Iterator<Cell> cellIterator, //
      SheetHandler handler) {
    List<String> datas = new ArrayList<>();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      String str = asString(cell);
      if (null == str) {
        str = "";
      }
      if (str.length() == 0) {
        datas.add(null);
      } else {
        datas.add(str);
      }
    }
    handler.handle(sheetName, rowIndex, datas);
  }

  public static String asString(Cell cell) {
    int type = cell.getCellType();
    switch (type) {
    case Cell.CELL_TYPE_STRING:
      return cell.getStringCellValue();
    case Cell.CELL_TYPE_BLANK:
      return "";
    case Cell.CELL_TYPE_NUMERIC:
      return String.valueOf(cell.getNumericCellValue());
    case Cell.CELL_TYPE_FORMULA:
      try {
        return cell.getStringCellValue();
      } catch (IllegalStateException ise) {
        return String.valueOf(Math.round(cell.getNumericCellValue()));
      }
    default:
      throw new IllegalArgumentException("Unknown cell type: " + cell.getCellType());
    }
  }

}
