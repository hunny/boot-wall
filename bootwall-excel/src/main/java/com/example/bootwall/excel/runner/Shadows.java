package com.example.bootwall.excel.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Shadows {

  public static final Map<String, String> METHODS = new HashMap<String, String>();

  static {
    // Key:method, Value:obfs|protocol|remarks_base64
    METHODS.put("aes-256-cfb", "plain|origin|");
    METHODS.put("aes-192-ctr", "tls1.2_ticket_auth|auth_sha1_v4|5L+E");
    METHODS.put("chacha20", "http_simple|auth_sha1_v4|5Z2hODA=");
    METHODS.put("aes-256-cfb", "plain|origin");
    METHODS.put("aes-256-cfb", "plain|origin");
  }

  public static List<ShadowConfig> removeRepeat(List<ShadowConfig> list) {
    Set<ShadowConfig> set = new HashSet<>();
    set.addAll(list);
    return new ArrayList<ShadowConfig>(set);
  }

}
