package com.example.bootwall.cdp4j.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bootwall.cdp4j.service.ErrorVideoService;

@Component
@EnableScheduling
@Profile("chat")
public class VideoErrorQuartz {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private ErrorVideoService errorVideoService;
  
  @Scheduled(fixedRate = 10 * 60 * 1000)
  public void cron() throws Exception {
    logger.info("查询错误的视频列表。");
    errorVideoService.run();
  }
  
}
