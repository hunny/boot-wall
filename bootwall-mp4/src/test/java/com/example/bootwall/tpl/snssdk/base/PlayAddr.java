package com.example.bootwall.tpl.snssdk.base;

import java.util.ArrayList;
import java.util.List;

public class PlayAddr {

  private final List<String> url_list = new ArrayList<>();
  private String uri; // ID

  public List<String> getUrl_list() {
    return url_list;
  }

  public void setUrl_list(List<String> url_list) {
    this.url_list.clear();
    if (null != url_list) {
      this.url_list.addAll(url_list);
    }
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

}
