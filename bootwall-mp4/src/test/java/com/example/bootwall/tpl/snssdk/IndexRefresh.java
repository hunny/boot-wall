package com.example.bootwall.tpl.snssdk;

import java.util.ArrayList;
import java.util.List;

import com.example.bootwall.tpl.snssdk.base.ShareInfo;
import com.example.bootwall.tpl.snssdk.base.Statistics;
import com.example.bootwall.tpl.snssdk.base.Status;
import com.example.bootwall.tpl.snssdk.base.Video;

public class IndexRefresh {

  private final List<Aweme> aweme_list = new ArrayList<>();

  public List<Aweme> getAweme_list() {
    return aweme_list;
  }

  public void setAweme_list(List<Aweme> aweme_list) {
    this.aweme_list.clear();
    if (null != aweme_list) {
      this.aweme_list.addAll(aweme_list);
    }
  }

  public static class Aweme {
    private Video video;
    private String aweme_id;
    private Statistics statistics;
    private String share_url;
    private Status status;
    private String desc;
    private ShareInfo share_info;

    public Video getVideo() {
      return video;
    }

    public void setVideo(Video video) {
      this.video = video;
    }

    public String getAweme_id() {
      return aweme_id;
    }

    public void setAweme_id(String aweme_id) {
      this.aweme_id = aweme_id;
    }

    public Statistics getStatistics() {
      return statistics;
    }

    public void setStatistics(Statistics statistics) {
      this.statistics = statistics;
    }

    public String getShare_url() {
      return share_url;
    }

    public void setShare_url(String share_url) {
      this.share_url = share_url;
    }

    public Status getStatus() {
      return status;
    }

    public void setStatus(Status status) {
      this.status = status;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public ShareInfo getShare_info() {
      return share_info;
    }

    public void setShare_info(ShareInfo share_info) {
      this.share_info = share_info;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Aweme [video=");
      builder.append(video);
      builder.append(", aweme_id=");
      builder.append(aweme_id);
      builder.append(", statistics=");
      builder.append(statistics);
      builder.append(", share_url=");
      builder.append(share_url);
      builder.append(", status=");
      builder.append(status);
      builder.append(", desc=");
      builder.append(desc);
      builder.append(", share_info=");
      builder.append(share_info);
      builder.append("]");
      return builder.toString();
    }

  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("IndexRefresh [aweme_list=");
    builder.append(aweme_list);
    builder.append("]");
    return builder.toString();
  }

}
