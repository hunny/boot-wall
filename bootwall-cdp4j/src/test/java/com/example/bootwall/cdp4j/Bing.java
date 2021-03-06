package com.example.bootwall.cdp4j;

import java.util.Locale;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class Bing {

  public static void main(String[] args) {
    Launcher launcher = new Launcher();

    try (SessionFactory factory = launcher.launch(); Session session = factory.create()) {
      session.navigate("https://www.bing.com") //
          .waitDocumentReady() //
          .installSizzle() //
          .enableNetworkLog() //
          .click("input[type='search']") //
          .sendKeys("Microsoft") //
          .sendEnter() //
          .wait(1000);

      String firstResult = session.getText("strong").toLowerCase(Locale.ENGLISH);

      System.out.println("Query String : " + session.getQueryString());
      System.out.println("Path name    : " + session.getPathname());

      System.out.println(firstResult);
    }
  }
}
