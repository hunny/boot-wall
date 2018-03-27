package com.example.bootwall.cdp4j.runner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

@Component
public class FreeSSRunner implements CommandLineRunner {

  public static final String IP_PATTERN = //
      "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
  
  @Value("${my.url}")
  private String url;
  
  @Override
  public void run(String... args) throws Exception {

    Launcher launcher = new Launcher();
    SessionFactory factory = launcher.launch();
    Session session = factory.create();
    session.getCommand().getNetwork().enable();
    session.navigate(args[0]);

    try {
      session.waitDocumentReady(30 * 1000);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
    if (session.isDomReady()) {
      System.err.println("===========================>");
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
          }
        }
      }
    }

  }

}
