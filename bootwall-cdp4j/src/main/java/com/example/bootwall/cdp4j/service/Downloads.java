package com.example.bootwall.cdp4j.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Downloads {
  
  private static final Logger logger = LoggerFactory.getLogger(Downloads.class);
  
  public static boolean download(String uuid, String link, String p) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet request = new HttpGet(link);
    request.setHeader("Accept-Type", "application/octet-stream");
    request.setHeader("Accept-Encoding", "identity;q=1, *;q=0");
    request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
    CloseableHttpResponse response = httpclient.execute(request);
    HttpEntity entity = response.getEntity();
    int responseCode = response.getStatusLine().getStatusCode();
    if (responseCode != 200) {
      throw new Exception("服务器响应代码：" + responseCode);
    }
    InputStream is = entity.getContent();
    String path = p + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/";
    if (!new File(path).exists()) {
      new File(path).mkdirs();
    }
    String filePath = path + uuid + ".mp4";
    FileOutputStream fos = new FileOutputStream(new File(filePath));
    int inByte;
    while ((inByte = is.read()) != -1) {
      fos.write(inByte);
    }
    is.close();
    fos.close();
    logger.info("下载完毕{}", filePath);
    return true;
  }

}
