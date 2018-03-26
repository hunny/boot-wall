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

  @Value("${my.url}")
  private String url;
  
  @Override
  public void run(String... args) throws Exception {

    Launcher launcher = new Launcher();
    SessionFactory factory = launcher.launch();
    Session session = factory.create();
    session.getCommand().getNetwork().enable();
    session.navigate(url);

    session.waitDocumentReady();
    if (session.isDomReady()) {
      System.err.println("===========================>");
      Document doc = Jsoup.parse(session.getContent());
      Elements elems = doc.select("table");
      for (Element elem : elems) {
        System.out.println("===========================");
        System.err.println(elem.html());
      }
    }

  }

}
