package com.example.bootwall.cdp4j.quartz;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bootwall.cdp4j.service.FreeSsService;

@Component
@EnableScheduling
public class FreessQuartz {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private FreeSsService freeSsService;
  
  @Scheduled(cron = "0 0/30 * * * ?") // 
  public void cron() throws Exception {
    logger.info("@Scheduled cron '{}', 时间'{}'", "0 0/30 * * * ?", new Date());
    freeSsService.run();
  }
  
}
