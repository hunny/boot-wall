package com.example.bootwall.cdp4j.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
public class ErrorVideoService {
  private final Logger logger = LoggerFactory.getLogger(ErrorVideoService.class);

  @Value("${video.wait.timeout:180000}")
  private int waitTimeout;
  @Autowired
  private VideoDao videoDao;

  public void run() throws Exception {
    try {
      Thread.sleep(40000);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    Launcher launcher = new Launcher();
    Path remoteProfileData = Paths.get(System.getProperty("java.io.tmpdir")) //
        .resolve("video-error-downloaded");
    try (SessionFactory factory = launcher.launch( //
        Arrays.asList("--user-data-dir=" + remoteProfileData.toString())); //
        Session session = factory.create()) {
      session.getCommand().getNetwork().enable();
      while (true) {
        List<Map<String, String>> list = videoDao.listErrorBy(10);
        if (null == list || list.isEmpty()) {
          session.close();
          return;
        }
        for (Map<String, String> u : list) {
          String uuid = u.get("uuid");
          String url = u.get("url");
          session.navigate(url);
          session.waitDocumentReady(waitTimeout);
          session.wait(1000);
          session.evaluate("document.getElementsByClassName('play-btn')[0].click();");
          session.wait(3000);
          Document doc = Jsoup.parse(session.getContent());
          Element text = doc.select(".video-info").first();
          Elements elems = doc.select("video");
          if (elems.size() == 0 && null != text) {
            String txt = text.text();
            txt = StringUtils.trim(txt);
            if (txt.equals("@")) {
              videoDao.update(uuid, "DELETE", null);
              continue;
            }
          }
          for (Element elem : elems) {
            String link = elem.attr("src");
            String info = null;
            if (null != text) {
              info = text.text();
            }
            if (null != info) {
              info = info.replaceAll("[^\\u0000-\\uFFFF]", "\uFFFD");
            }
            logger.info("准备从官方下载{}", link);
            try {
              Downloads.download(uuid, link, "/Users/hunnyhu/Downloads/download/");
              videoDao.update(uuid, "OK_D", info);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
    }

  }

}
