package com.example.bootwall.cdp4j;

import static java.util.Arrays.asList;

import java.util.List;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class BingTranslator {

  public static void main(String[] args) {
    Launcher launcher = new Launcher();

    List<String> words = asList("hello", "world");

    try (SessionFactory factory = launcher.launch(); Session session = factory.create()) {
      session.navigate("https://www.bing.com/translator") //
          .waitDocumentReady() //
          .enableConsoleLog() //
          .enableDetailLog() //
          .enableNetworkLog(); //

      session.click(".t_in select") // click source language
          .wait(500) //
          .click(".t_in option[value='en']") // choose English
          .wait(500) //
          .click(".t_out select") // click destination language
          .wait(500) //
          .click(".t_out option[value='zh-CHS']") // choose Estonian
          .wait(500); //

      StringBuilder builder = new StringBuilder(); //

      for (String word : words) {
        String text = session //
            .focus(".t_in .b_focusTextMedium") //
            .selectInputText(".t_in .b_focusTextMedium") //
            .sendBackspace() //
            .sendKeys(word) //
            .wait(1000) //
            .getText("#t_txtoutblk"); //

        builder.append(text).append(" "); //
      }

      System.out.println(builder.toString());
    }
  }
}
