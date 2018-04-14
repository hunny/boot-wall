package com.example.bootwall.cdp4j.dao;

public class Video {

  private String uuid;
  private String url;
  private String downloaded;
  private String type;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDownloaded() {
    return downloaded;
  }

  public void setDownloaded(String downloaded) {
    this.downloaded = downloaded;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Video [uuid=");
    builder.append(uuid);
    builder.append(", url=");
    builder.append(url);
    builder.append(", downloaded=");
    builder.append(downloaded);
    builder.append(", type=");
    builder.append(type);
    builder.append("]");
    return builder.toString();
  }

}
