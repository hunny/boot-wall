package com.example.bootwall.tpl.snssdk;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Snssdks {
  
  public static final String URL = "https://api.amemv.com";
  
  // 首页推荐刷新
  public static final String INDEX_RECOMMAND_REFRESH = "/aweme/v1/feed/";
  
  public static void feed(String file) throws Exception {
    List<String> lines = IOUtils.readLines(new FileInputStream(file), "UTF-8");
    StringBuffer buffer = new StringBuffer();
    for (String line : lines) {
      buffer.append(line);
    }
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure( //
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    IndexRefresh refresh = objectMapper.readValue( //
        buffer.toString(), IndexRefresh.class);
    System.out.println(objectMapper.writeValueAsString(refresh));
    if (null == refresh.getAweme_list()) {
      return;
    }
  }
  
  public static void main(String[] args) throws Exception {
    feed("/Users/hunnyhu/Desktop/feed.json");
  }

}
