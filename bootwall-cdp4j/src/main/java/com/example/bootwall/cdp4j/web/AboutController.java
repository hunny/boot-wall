package com.example.bootwall.cdp4j.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootwall.cdp4j.service.VideoTextService;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private VideoTextService videoTextService;
  
  @GetMapping("/about")
  public ResponseEntity<String> getAbout() {
    
    logger.info("Receive about request.");
    
    return new ResponseEntity<String>("Template Project", HttpStatus.OK);
  }
  
  @GetMapping("/fetch/{table}")
  public ResponseEntity<String> fetchText( //
      @PathVariable("table") String table) {
    logger.info("Receive fetchText request.");
    try {
      videoTextService.run(table);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ResponseEntity<String>(table + " execute Project", HttpStatus.OK);
  }
  
}
