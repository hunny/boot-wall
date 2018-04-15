package com.example.bootwall.cdp4j.service;

import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bootwall.cdp4j.dao.Video;
import com.example.bootwall.cdp4j.dao.VideoDao;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

@Service
public class ChatReaderService {

  private Logger logger = LoggerFactory.getLogger(ChatReaderService.class);

  private String url = "https://wx2.qq.com/";

  @Autowired
  private VideoDao videoDao;

  public void read() {
    Launcher launcher = new Launcher();
    Path remoteProfileData = Paths.get(System.getProperty("java.io.tmpdir")) //
        .resolve("wx2-qq");
    try (SessionFactory factory = launcher.launch( //
        Arrays.asList("--user-data-dir=" + remoteProfileData.toString())); //
        Session session = factory.create()) {
      session.getCommand().getNetwork().enable();
      session.navigate(url);
      session.waitDocumentReady(30 * 1000);
      while (true) {
        Document doc = Jsoup.parse(session.getContent());
        Elements elems = doc.select("div.bubble_cont");
        for (Element elem : elems) {
          Element href = elem.select("a[href]").first();
          if (null == href) {
            continue;
          }
          Video video = parse(href.attr("href"));
          if (null != video) {
            if (StringUtils.isBlank(videoDao.getBy(video.getUuid()))) {
              logger.info("新增内容{}。", video);
              videoDao.insert(video);
            }
          }
        }
        session.wait(1000);
      }
    }
  }

  public static Pattern URL_PATTERN = Pattern.compile("/cgi-bin/mmwebwx-bin/webwxcheckurl\\?requrl=(.*)");
  public static Pattern URL_UUID = Pattern.compile("https://www.iesdouyin.com/share/video/(\\d+)/");

  public static Video parse(String href) {
    String url = match(href, URL_PATTERN);
    Video video = new Video();
    try {
      url = URLDecoder.decode(url, "UTF-8");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (null != url && url.contains("douyin.com/share/video")) {
      url = url.split("\\?")[0];
      String uuid = match(url, URL_UUID);
      if (null != uuid) {
        video.setUuid(uuid);
      } else {
        video.setUuid(url);
      }
      video.setUrl(url);
    }
    if (StringUtils.isBlank(video.getUuid())) {
      return null;
    }
    return video;
  }

  public static String match(String str, Pattern ptn) {
    Matcher matcher = ptn.matcher(str);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  // public static void main(String[] args) {
  // parse(null);
  // }

}
