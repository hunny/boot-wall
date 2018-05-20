package com.example.bootwall.cdp4j.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("chat")
public class VideoTextRunner implements CommandLineRunner {

//  @Autowired
//  private VideoTextService videoTextService;

  @Override
  public void run(String... args) throws Exception {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
//          videoTextService.run();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

}
