package com.example.bootwall.cdp4j.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
public class VideoTextService {

  private final Logger logger = LoggerFactory.getLogger(VideoTextService.class);

  @Value("${video.wait.timeout:180000}")
  private int waitTimeout;
  @Autowired
  private VideoDao videoDao;

  public void run(final String table) throws Exception {
    try {
      Thread.sleep(30000);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    Launcher launcher = new Launcher();
    Path remoteProfileData = Paths.get(System.getProperty("java.io.tmpdir")) //
        .resolve("video-text-downloaded");
    try (SessionFactory factory = launcher.launch( //
        Arrays.asList("--user-data-dir=" + remoteProfileData.toString())); //
        Session session = factory.create()) {
      session.getCommand().getNetwork().enable();
      while (true) {
        List<Map<String, String>> list = videoDao.listTextBy(table, 100);
        if (null == list || list.isEmpty()) {
          session.close();
          return;
        }
        for (Map<String, String> u : list) {
          String uuid = u.get("uuid");
          String url = u.get("url");
          session.navigate(url);
          session.waitDocumentReady(waitTimeout);
          session.wait(new Random().nextInt(5000));
          String content = (session.getContent());
          if (null == content) {
            continue;
          }
          Document doc = Jsoup.parse(content);
          Element text = doc.select(".video-info").first();
          if (null != text) {
            String txt = text.text();
            txt = StringUtils.trim(txt);
            if (txt.length() > 1024) {
              txt = txt.substring(0, 1024);
            }
            if (null != txt) {
              txt = txt.replaceAll("[^\\u0000-\\uFFFF]", "\uFFFD");
            }
            logger.info("更新文本信息[{}]：[{}]", uuid, txt);
            videoDao.updateText(table, uuid, txt);
          } else {
            Element iframe = doc.select("iframe").first();
            if (null != iframe //
                && "https://www.douyin.com/".equals(iframe.attr("src"))) {
              logger.info("地址已经被封，请更换代理。");
              session.close();
              return;
            }
          }
        }
      }
    }

  }
}
