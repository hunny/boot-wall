package com.example.bootwall.cdp4j.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bootwall.cdp4j.service.VideoTextService;

@Component
@EnableScheduling
@Profile("chat")
public class VideoTextQuartz {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private VideoTextService videoTextService;

  @Scheduled(fixedRate = 10 * 60 * 1000)
  public void cron() throws Exception {
    logger.info("查询没有文本的视频列表。");
    videoTextService.run();
  }

}
