package com.example.bootwall.cdp4j.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bootwall.cdp4j.dao.FreeSsDao;

@Service
public class FreeSsService {

  private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Autowired
  private FreeSsDao freeSsDao;

  @Transactional
  public void insertIfAbsent(List<String> list) {
    String ip = list.get(0);
    String port = list.get(1);
    String check = freeSsDao.getBy(ip, port);
    if (null == check) {
      list.set(6, dateFormat.format(new Date()) + " " + list.get(6));
      freeSsDao.insert(list);
    }
  }

}
