package com.example.bootwall.excel.runner;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Shadow {

  private boolean random = false;
  private String authPass = null;
  private boolean useOnlinePac = false;
  @JsonProperty(value = "TTL")
  private int TTL = 0;
  private boolean global = false;
  private int reconnectTimes = 3;
  private int index = 0;
  private int proxyType = 0;
  private String proxyHost = null;
  private String authUser = null;
  private String proxyAuthPass = null;
  private boolean isDefault = false;
  private String pacUrl = null;
  private int proxyPort = 0;
  private int randomAlgorithm = 0;
  private boolean proxyEnable = false;
  private boolean enabled = true;
  private boolean autoban = false;
  private String proxyAuthUser = null;
  private boolean shareOverLan = false;
  private int localPort = 1080;
  private final List<ShadowConfig> configs = new ArrayList<>();
  public boolean isRandom() {
    return random;
  }
  public void setRandom(boolean random) {
    this.random = random;
  }
  public String getAuthPass() {
    return authPass;
  }
  public void setAuthPass(String authPass) {
    this.authPass = authPass;
  }
  public boolean isUseOnlinePac() {
    return useOnlinePac;
  }
  public void setUseOnlinePac(boolean useOnlinePac) {
    this.useOnlinePac = useOnlinePac;
  }
  public int getTTL() {
    return TTL;
  }
  public void setTTL(int tTL) {
    TTL = tTL;
  }
  public boolean isGlobal() {
    return global;
  }
  public void setGlobal(boolean global) {
    this.global = global;
  }
  public int getReconnectTimes() {
    return reconnectTimes;
  }
  public void setReconnectTimes(int reconnectTimes) {
    this.reconnectTimes = reconnectTimes;
  }
  public int getIndex() {
    return index;
  }
  public void setIndex(int index) {
    this.index = index;
  }
  public int getProxyType() {
    return proxyType;
  }
  public void setProxyType(int proxyType) {
    this.proxyType = proxyType;
  }
  public String getProxyHost() {
    return proxyHost;
  }
  public void setProxyHost(String proxyHost) {
    this.proxyHost = proxyHost;
  }
  public String getAuthUser() {
    return authUser;
  }
  public void setAuthUser(String authUser) {
    this.authUser = authUser;
  }
  public String getProxyAuthPass() {
    return proxyAuthPass;
  }
  public void setProxyAuthPass(String proxyAuthPass) {
    this.proxyAuthPass = proxyAuthPass;
  }
  public boolean isDefault() {
    return isDefault;
  }
  public void setDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }
  public String getPacUrl() {
    return pacUrl;
  }
  public void setPacUrl(String pacUrl) {
    this.pacUrl = pacUrl;
  }
  public int getProxyPort() {
    return proxyPort;
  }
  public void setProxyPort(int proxyPort) {
    this.proxyPort = proxyPort;
  }
  public int getRandomAlgorithm() {
    return randomAlgorithm;
  }
  public void setRandomAlgorithm(int randomAlgorithm) {
    this.randomAlgorithm = randomAlgorithm;
  }
  public boolean isProxyEnable() {
    return proxyEnable;
  }
  public void setProxyEnable(boolean proxyEnable) {
    this.proxyEnable = proxyEnable;
  }
  public boolean isEnabled() {
    return enabled;
  }
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  public boolean isAutoban() {
    return autoban;
  }
  public void setAutoban(boolean autoban) {
    this.autoban = autoban;
  }
  public String getProxyAuthUser() {
    return proxyAuthUser;
  }
  public void setProxyAuthUser(String proxyAuthUser) {
    this.proxyAuthUser = proxyAuthUser;
  }
  public boolean isShareOverLan() {
    return shareOverLan;
  }
  public void setShareOverLan(boolean shareOverLan) {
    this.shareOverLan = shareOverLan;
  }
  public int getLocalPort() {
    return localPort;
  }
  public void setLocalPort(int localPort) {
    this.localPort = localPort;
  }
  public List<ShadowConfig> getConfigs() {
    return configs;
  }
  public void setConfigs(List<ShadowConfig> configs) {
    this.configs.clear();
    if (null != configs) {
      this.configs.addAll(configs);
    }
  }
  
}
