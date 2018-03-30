package com.example.bootwall.cdp4j.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.bootwall.cdp4j.druid.DruidConfigValue;

@ConfigurationProperties(prefix = "spring.datasource")
@Component
@ConditionalOnMissingBean(DruidConfigValue.class)
public class DefaultDaoConfigValue {

  private String url;
  private String username;
  private String password;
  private String driverClassName;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

}
