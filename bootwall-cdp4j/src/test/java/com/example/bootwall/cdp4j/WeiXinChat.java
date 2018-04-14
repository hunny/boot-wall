package com.example.bootwall.cdp4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class WeiXinChat {

  public static void main(String[] args) {
    Launcher launcher = new Launcher();

    try (SessionFactory factory = launcher.launch(); Session session = factory.create()) {
      session.getCommand().getNetwork().enable();
      session.navigate("https://wx2.qq.com/");
      session.waitDocumentReady(30 * 1000);
      for (int i = 0; i < 90; i++) {
        Document doc = Jsoup.parse(session.getContent());
        Elements elems = doc.select("div.bubble_cont");
        for (Element elem : elems) {
          Element href = elem.select("a[href]").first();
          if (null != href) {
            System.err.println(href.text());
          }
        }
        session.wait(1000);
      }
    }
  }
}