package com.example.bootwall.cdp4j;

import java.text.MessageFormat;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.event.Events;
import io.webfolder.cdp.event.network.LoadingFinished;
import io.webfolder.cdp.event.network.ResponseReceived;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class Crawler {
  
  public static void main(String[] args) {
    
    Launcher launcher = new Launcher();
    try (SessionFactory factory = launcher.launch(); Session session = factory.create()) {
      session.getCommand().getNetwork().enable();
      session.addEventListener((e, d) -> {
        if (Events.NetworkLoadingFinished.equals(e)) {
          LoadingFinished lf = (LoadingFinished) d;
          System.out.println("NetworkLoadingFinished:" + lf.getRequestId());
        }
        if (Events.NetworkResponseReceived.equals(e)) {
          ResponseReceived rr = (ResponseReceived) d;
          System.out.println("NetworkLoadingFinished:" + rr.getRequestId());
          System.err.println( //
              MessageFormat.format("地址：{0}", rr.getResponse().getUrl()));
        }
      });
      session.navigate("https://www.baidu.com");
      session.waitDocumentReady(30* 1000);
      String title = session.getText("/html/head/title");
      System.out.println("=========>" + title);
    }
    
  }
}
