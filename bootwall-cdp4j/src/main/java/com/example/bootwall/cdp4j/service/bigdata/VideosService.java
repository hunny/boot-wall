package com.example.bootwall.cdp4j.service.bigdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootwall.cdp4j.dao.DBUtils;

public class VideosService {

  private static final Logger LOGGER = LoggerFactory.getLogger(VideosService.class);

  public static void video() throws Exception {
    Document doc = Jsoup.parse(new URL("https://kolranking.com/douyin/videos"), 5000);
    Elements trs = doc.select("tr");
    for (Element tr : trs) {
      Elements tds = tr.select("td");
      if (tds.size() == 9) {
        System.out.println("===================");
        Element href = tds.select("a").first();
        String url = "https://kolranking.com/" + href.attr("href");
        System.out.println(url);
        Document doc2 = Jsoup.parse(new URL(url), 5000);
        Elements trs2 = doc2.select("tr");
        for (Element tr2 : trs2) {
          Elements tds2 = tr2.select("td");
          Element elem = tds2.get(0);
          if ("视频导出".equals(elem.text())) {
            Element elemHref = tr2.select("a").first();
            String download = elemHref.attr("href");
            String uuid = download.split("=")[1];
            download(download, "D:/testvideo/", uuid);
          }
        }
      }
    }
  }

  public static void userCategory() throws Exception {
    DBUtils.db((Connection conn, Statement stmt) -> {
      for (int i = 1; i < 34; i++) {
        try {
          findCategory(stmt, "" + i);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private static void findCategory(Statement stmt, String index) throws Exception {
    String l = "https://kolranking.com/douyin/users/category/" + index;
    LOGGER.info("Request URL:{}", l);
    Document doc = Jsoup.parse(new URL(l), 10000);
    Elements trs = doc.select("tr");
    for (Element tr : trs) {
      Elements tds = tr.select("td");
      if (tds.size() == 7) {
        System.out.println("===================");
        Element elem1 = tds.get(1);
        String image = elem1.select("img").attr("src");
        Element href = tds.get(2).selectFirst("a");
        String url = "https://kolranking.com" + href.attr("href");
        LOGGER.info("analyse URL:{}", url);
        String nickname = tds.get(2).text();
        String sex = tds.get(3).text();
        String fans = tds.get(4).text();
        String likes = tds.get(5).text();
        String videos = tds.get(6).text();

        String sql = "SELECT * FROM videoUserCategory WHERE url = '" //
            + url //
            + "' LIMIT 1;";
        ResultSet rs = stmt.executeQuery(sql);
        // STEP 5: Extract data from result set
        if (!rs.next()) { // 没有结果
          stmt.executeUpdate(new StringBuilder() //
              .append(
                  "insert into videoUserCategory (url, nickname, sex, fans, likes, videos, image, dateCreated, lastUpdated) values (") //
              .append("'") //
              .append(StringUtils.join(new String[] { //
                  url.replaceAll("'", "''"), //
                  nickname.replaceAll("'", "''").replaceAll("[^\\u0000-\\uD7FF\\uE000-\\uFFFF]",
                      "?"), //
                  sex.replaceAll("'", "''"), //
                  fans.replaceAll("'", "''"), //
                  likes.replaceAll("'", "''"), //
                  videos.replaceAll("'", "''"), //
                  image.replaceAll("'", "''"), //
              }, "','")) //
              .append("'") //
              .append(", now(), now())") //
              .append("") //
              .toString()); //
        }
        // STEP 6: Clean-up environment
        DBUtils.close(rs);
      }
    }
  }

  public static void download(String link, String dir, String uuid) throws Exception {
    HttpGet request = new HttpGet(link);
    CloseableHttpClient httpclient = HttpClients.createDefault();
    request.setHeader("Accept-Type", "application/octet-stream");
    CloseableHttpResponse response = httpclient.execute(request);
    HttpEntity entity = response.getEntity();
    int responseCode = response.getStatusLine().getStatusCode();
    if (responseCode != 200) {
      throw new Exception("服务器响应代码：" + responseCode);
    }
    InputStream is = entity.getContent();
    // dir = "/Users/hunnyhu/Downloads/download/";
    String path = dir + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/";
    if (!new File(path).exists()) {
      new File(path).mkdirs();
    }
    String filePath = path + uuid + ".mp4";
    if (new File(filePath).exists()) {
      LOGGER.info("文件已经存在，忽略下载{}", filePath);
      return;
    }
    FileOutputStream fos = new FileOutputStream(new File(filePath));
    int inByte;
    while ((inByte = is.read()) != -1) {
      fos.write(inByte);
    }
    is.close();
    fos.close();
    LOGGER.info("下载完毕{}", filePath);
  }

  public static void main(String[] args) throws Exception {
    VideosService.userCategory();
    VideosService.video();
  }

}
