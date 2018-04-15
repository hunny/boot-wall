package com.example.bootwall.cdp4j.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.bootwall.cdp4j.dao.VideoDao;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

@Service
public class VideoService {

  private final Logger logger = LoggerFactory.getLogger(VideoService.class);
  
  @Value("${video.wait.timeout:180000}")
  private int waitTimeout;

  @Value("${video.url:}")
  private String url;

  @Autowired
  private VideoDao videoDao;

  public static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

  public void run() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    Launcher launcher = new Launcher();
    Path remoteProfileData = Paths.get(System.getProperty("java.io.tmpdir")) //
        .resolve("video-downloaded");
    try (SessionFactory factory = launcher.launch( //
        Arrays.asList("--user-data-dir=" + remoteProfileData.toString())); //
        Session session = factory.create()) {
      session.getCommand().getNetwork().enable();
      session.navigate(url);
      session.waitDocumentReady(3 * 60 * 1000);
      while (true) {
        List<Map<String, String>> list = videoDao.listBy(100);
        for (Map<String, String> u : list) {
          session.evaluate("document.getElementsByTagName('input')[0].focus();");
          session.evaluate("document.getElementsByTagName('input')[0].value = '';");
          session.sendKeys(u.get("url"));
          session.sendEnter();
          session.wait(3 * 1000);
          final String content = session.getContent();
          try {
            parse(content, u.get("uuid"));
            videoDao.update(u.get("uuid"), "OK");
          } catch (Exception e) {
            videoDao.update(u.get("uuid"), "ERROR");
            e.printStackTrace();
          }
        }
      }
    }
  }

  protected void asyn(String content, final String uuid) throws Exception {
    Future<?> future = fixedThreadPool.submit(new Runnable() {
      @Override
      public void run() {
        try {
          parse(content, uuid);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    try {
      future.get();
    } catch (Exception e) {
      throw new Exception("超时。");
    }
  }

  private void parse(String content, String uuid) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    Document doc = Jsoup.parse(content);
    Elements elems = doc.select("a");
    for (Element elem : elems) {
      if ("下载视频".equals(elem.text())) {
        String link = elem.attr("href");
        logger.info("准备下载{}", link);
        HttpGet request = new HttpGet(link);
        request.setHeader("Accept-Type", "application/octet-stream");
        CloseableHttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode != 200) {
          throw new Exception("服务器响应代码：" + responseCode);
        }
        InputStream is = entity.getContent();
        String path = "/Users/hunnyhu/Downloads/download/" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/";
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
      }
    }
  }

}
