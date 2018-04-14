package com.example.bootwall.cdp4j.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bootwall.cdp4j.dao.FreeSsDao;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

@Service
public class FreeSsService {

  public static final String IP_PATTERN = //
      "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";

  private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Value("${freess.wait.timeout:180000}")
  private int waitTimeout;
  
  @Value("${freess.url:}")
  private String url;

  @Autowired
  private FreeSsDao freeSsDao;

  @Transactional
  public void insertIfAbsent(List<String> list) {
    String ip = list.get(0);
    String port = list.get(1);
    String check = freeSsDao.getBy(ip, port);
    if (null == check) {
      list.set(6, dateFormat.format(new Date()) + " " + list.get(6));
      freeSsDao.insert(list);
    }
  }

  @Transactional
  public void run() throws Exception {
    Session session = createSession(url);
    if (session.isDomReady()) {
      System.err.println("===========================>");
      Thread.sleep(2000);
      parse(session);
    }
  }

  private void parse(Session session) {
    Document doc = Jsoup.parse(session.getContent());
    Elements elems = doc.select("table");
    for (Element elem : elems) {
      System.out.println("===========================");
      Elements trs = elem.select("tr[role=\"row\"][class]");
      for (Element tr : trs) {
        Elements tds = tr.select("td");
        if (tds.size() >= 2 //
            && tds.get(1).text() //
                .matches(IP_PATTERN)) {
          System.out.println(tds.html());
          String ip = tds.get(1).text();
          String port = tds.get(2).text();
          List<String> list = new ArrayList<>();
          list.add(ip);
          list.add(port);//
          list.add(tds.get(3).text());
          list.add(tds.get(4).text());
          list.add(tds.get(0).text());
          list.add(tds.get(6).text());
          list.add(tds.get(5).text());
          insertIfAbsent(list);
        }
      }
    }
  }

  private Session createSession(String url) {
    Launcher launcher = new Launcher();
    Path remoteProfileData = Paths.get(System.getProperty("java.io.tmpdir")) //
        .resolve(UUID.randomUUID().toString());
    SessionFactory factory = launcher.launch(Arrays.asList("--user-data-dir=" + remoteProfileData.toString()));
    Session session = factory.create();
    session.getCommand().getNetwork().enable();
    session.navigate(url);

    try {
      session.waitDocumentReady(waitTimeout);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
    return session;
  }

}
