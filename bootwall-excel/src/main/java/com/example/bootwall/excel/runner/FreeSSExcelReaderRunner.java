package com.example.bootwall.excel.runner;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bootwall.excel.service.ExcelReaderService;
import com.example.bootwall.excel.service.SheetHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class FreeSSExcelReaderRunner implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ExcelReaderService excelReaderSerivce;

  private ObjectMapper objectMapper = new ObjectMapper();
  
  @Value("${bootwall-excel.shadow.json:}")
  private String jsonFile;
  
  @Value("${bootwall-excel.shadow.excel:}")
  private String excelFile;

  @Override
  public void run(String... args) throws Exception {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    Shadow shadow = objectMapper.readValue(//
        new FileInputStream(new File(jsonFile)), Shadow.class);
    excelReaderSerivce.read(excelFile, new SheetHandler() {
      @Override
      public void handle(String name, int row, List<String> datas) {
        logger.info("sheetName:{}", name);
        logger.info("rowIndex:{}", row);
        logger.info("values:{}", datas);
        if (name.matches("\\d+") && row > 0 && datas.size() == 7) {
          // IP
          shadow.getConfigs().add(new ShadowConfig( //
              datas.get(1), // IP
              Integer.parseInt(datas.get(2)), //
              datas.get(3), //
              datas.get(4), //
              null, //
              datas.get(6) //
          ) //
          );
        }
      }
    });
    shadow.setConfigs(Shadows.removeRepeat(shadow.getConfigs()));
    objectMapper.writeValue(new File(jsonFile), shadow);
    System.out.println(objectMapper.writeValueAsString(shadow));
    System.exit(0);
  }

}
