package com.example.bootwall.tpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Refresh {

  private List<Aweme> aweme_list;

  public List<Aweme> getAweme_list() {
    return aweme_list;
  }

  public void setAweme_list(List<Aweme> aweme_list) {
    this.aweme_list = aweme_list;
  }

  public static class Aweme {
    private Video video;
    private String aweme_id;
    private String share_url;
    private String title;

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

    public String getShare_url() {
      return share_url;
    }

    public void setShare_url(String share_url) {
      this.share_url = share_url;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    @Override
    public String toString() {
      return "Aweme [video=" + video + ", aweme_id=" + aweme_id + ", share_url=" + share_url
          + ", title=" + title + "]";
    }

  }

  public static class Video {
    private PlayAddr play_addr;

    public PlayAddr getPlay_addr() {
      return play_addr;
    }

    public void setPlay_addr(PlayAddr play_addr) {
      this.play_addr = play_addr;
    }

    @Override
    public String toString() {
      return "Video [play_addr=" + play_addr + "]";
    }

  }

  public static class PlayAddr {
    private List<String> url_list;
    private String uri;

    public List<String> getUrl_list() {
      return url_list;
    }

    public void setUrl_list(List<String> url_list) {
      this.url_list = url_list;
    }

    public String getUri() {
      return uri;
    }

    public void setUri(String uri) {
      this.uri = uri;
    }

    @Override
    public String toString() {
      return "PlayAddr [url_list=" + url_list + ", uri=" + uri + "]";
    }

  }

  public static class Statistics {
    private long digg_count;
    private long share_count;
    private long comment_count;

    public long getDigg_count() {
      return digg_count;
    }

    public void setDigg_count(long digg_count) {
      this.digg_count = digg_count;
    }

    public long getShare_count() {
      return share_count;
    }

    public void setShare_count(long share_count) {
      this.share_count = share_count;
    }

    public long getComment_count() {
      return comment_count;
    }

    public void setComment_count(long comment_count) {
      this.comment_count = comment_count;
    }

    @Override
    public String toString() {
      return "Statistics [digg_count=" + digg_count + ", share_count=" + share_count
          + ", comment_count=" + comment_count + "]";
    }
  }

  @Override
  public String toString() {
    return "Refresh [aweme_list=" + aweme_list + "]";
  }

  public static void main(String[] args) throws Exception {
    download(new File("C:/Users/huzexiong/Desktop/refresh.md"));
  }

  public static void download(String str) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure( //
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Refresh refresh = objectMapper.readValue(str, Refresh.class);
    System.out.println(refresh);
    if (null == refresh.getAweme_list()) {
      return;
    }
    download(refresh);
  }

  public static void download(File file) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure( //
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Refresh refresh = objectMapper.readValue(file, Refresh.class);
    System.out.println(refresh);
    download(refresh);
  }

  public static void download(Refresh refresh) throws IOException {
    List<Aweme> awemes = refresh.getAweme_list();
    for (Aweme aweme : awemes) {
      System.out.println("============ share url: " + aweme.getShare_url());
      Video video = aweme.getVideo();
      PlayAddr play_addr = video.getPlay_addr();
      // for (String url : play_addr.getUrl_list()) {
      String url = play_addr.getUrl_list().get(0);
      System.out.println("video url: " + url);
      Response document = Jsoup.connect(url).ignoreContentType(true).timeout(8000).execute();
      BufferedInputStream stream = document.bodyStream();
      FileUtils.copyInputStreamToFile(stream, //
          new File( //
              MessageFormat.format("C:/Users/huzexiong/Desktop/downloads/{0}.mp4", //
                  String.valueOf(new Date().getTime()))));
      System.out.println("下载完毕 url: " + url);
      // break;
      // }
    }
  }

}
