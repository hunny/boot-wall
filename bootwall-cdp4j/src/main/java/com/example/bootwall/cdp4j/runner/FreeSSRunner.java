package com.example.bootwall.cdp4j.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.bootwall.cdp4j.service.FreeSsService;

@Component
@Profile("freess")
public class FreeSSRunner implements CommandLineRunner {

  @Autowired
  private FreeSsService freeSsService;

  @Override
  public void run(String... args) throws Exception {
    freeSsService.run();
  }

}
