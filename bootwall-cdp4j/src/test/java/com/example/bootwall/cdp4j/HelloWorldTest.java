package com.example.bootwall.cdp4j;

import org.junit.Test;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class HelloWorldTest {

  @Test
  public void printTextTest() {
    Launcher launcher = new Launcher();
    try (SessionFactory factory = launcher.launch(); Session session = factory.create()) {
      session.navigate("https://webfolder.io");
      session.waitDocumentReady();
      String content = (String) session.getProperty("//body", "outerText");
      System.out.println(content);
    }
  }

}
