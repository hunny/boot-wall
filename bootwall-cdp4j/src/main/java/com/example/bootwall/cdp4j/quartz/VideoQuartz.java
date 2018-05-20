package com.example.bootwall.cdp4j.quartz;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@EnableAsync
public class VideoQuartz {

//  private final Logger logger = LoggerFactory.getLogger(getClass());
  
//  @Autowired
//  private ChatReaderService chatReaderService;
  
  @Async
  @Scheduled(fixedRate = 1000)
  public void cron() throws Exception {
//    logger.info("查询消息。");
//    chatReaderService.read();
  }
  
}
