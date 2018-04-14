package com.example.bootwall.cdp4j;

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
      Object obj = session.evaluate("var ok = function() {return window.document.body.text;}; ok();");
      System.err.println(obj);
      
      Double result = (Double) session.evaluate("var foo = function() { return 2 + 2; }; foo();");
      System.err.println(result);

      session.evaluate("var bar = {}; bar.myFunc = function(s1, s2) { return s1 + ' ' + s2; }");
      String message = session.callFunction("bar.myFunc", String.class, "hello", "world");
      System.err.println(message);

      Integer intResult = session.callFunction("foo", Integer.class);
      System.err.println(intResult);
      session.wait(2000);
    }
  }
}