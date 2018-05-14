package com.example.bootwall.cdp4j.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
          session.wait(1000);
          session.sendEnter();
          session.wait(3 * 1000);
          try {
            if (parse(session, u.get("uuid"))) {
              videoDao.update(u.get("uuid"), "OK");
            }
          } catch (Exception e) {
            videoDao.update(u.get("uuid"), "ERROR");
            e.printStackTrace();
          }
        }
      }
    }
  }


  private boolean parse(final Session session, String uuid) throws Exception {
    int times = 0;
    while (times < 10) {
      Document doc = Jsoup.parse(session.getContent());
      Elements elems = doc.select("a");
      for (Element elem : elems) {
        if ("下载视频".equals(elem.text())) {
          String link = elem.attr("href");
          logger.info("准备下载{}", link);
          Downloads.download(uuid, link, "/Users/hunnyhu/Downloads/download/");
          return true;
        }
      }
      elems = doc.select("div.alert-danger");
      for (Element elem : elems) {
        if (elem.text().equals("解析失败,请检查该视频是否已删除") //
            || elem.text().equals("解析失败,请检查输入的链接页面是否含有视频")) {
          videoDao.update(uuid, "DELETE");
          return false;
        }
      }
      times++;
      logger.info("将等待三秒钟，准备重试{}，第{}次。", uuid, times);
      Thread.sleep(3 * 1000);// 等待三秒钟
    }
    return false;
  }

}
