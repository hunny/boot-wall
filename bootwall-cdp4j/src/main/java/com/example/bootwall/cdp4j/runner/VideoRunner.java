package com.example.bootwall.cdp4j.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.bootwall.cdp4j.service.VideoService;

@Component
@Profile("chat")
public class VideoRunner implements CommandLineRunner {

  @Autowired
  private VideoService videoService;

  @Override
  public void run(String... args) throws Exception {
    new Thread(new Runnable() {
      @Override
      public void run() {
        videoService.run();
      }
    }).start();
  }

}
