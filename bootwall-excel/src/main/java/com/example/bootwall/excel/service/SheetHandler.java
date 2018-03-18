package com.example.bootwall.excel.service;

import java.util.List;

public interface SheetHandler {

  void handle(String name, int row, List<String> datas);
  
}
