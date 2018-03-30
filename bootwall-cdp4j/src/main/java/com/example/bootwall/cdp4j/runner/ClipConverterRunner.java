package com.example.bootwall.cdp4j.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

@Component
@Profile("clip")
public class ClipConverterRunner implements CommandLineRunner {

  @Value("${clip.url}")
  private String url;
  
  @Value("${clip.target.url}")
  private String targeUrl;
  
  @Value("${clip.wait.timeout:30000}")
  private int waitTimeout;

  @Override
  public void run(String... args) throws Exception {
    Launcher launcher = new Launcher();
    SessionFactory factory = launcher.launch();
    Session session = factory.create();
    session.getCommand().getNetwork().enable();
    session.navigate(url);
    try {
      session.waitDocumentReady(30 * 1000);
      boolean succeed = session.waitUntil(s -> {
        return s.matches("#mediaurl");
      }, waitTimeout);
      if (succeed) {//
        session.setValue("#mediaurl", targeUrl);
        session.click("label[for=\"MP4\"]");
        session.click("#submiturl");
      }
      
      Thread.sleep(2000);
      succeed = session.waitUntil(s -> {
        return s.matches("input[value=\"Start!\"]");
      }, waitTimeout);
      session.click("input[value=\"Start!\"]");
      
      Thread.sleep(2000);
      succeed = session.waitUntil(s -> {
        return s.matches("#downloadbutton");
      }, waitTimeout);
      session.click("#downloadbutton");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.exit(0);
    }
  }

}
