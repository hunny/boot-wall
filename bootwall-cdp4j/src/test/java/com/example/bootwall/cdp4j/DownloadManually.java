package com.example.bootwall.cdp4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class DownloadManually {

  private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);

  public static void main(String[] args) throws Exception {
    Launcher launcher = new Launcher();

    try (SessionFactory factory = launcher.launch(); Session session = factory.create()) {
      session.getCommand().getNetwork().enable();
      session.navigate(args[0]);
      session.waitDocumentReady(3 * 60 * 1000);
      String[] urls = new String[] { //
           };
      for (String u : urls) {
        session.evaluate("document.getElementsByTagName('input')[0].focus();");
        session.evaluate("document.getElementsByTagName('input')[0].value = '';");
        session.sendKeys(u);
        session.sendEnter();
        session.wait(2 * 1000);
        final String content = session.getContent();
        fixedThreadPool.execute(new Runnable() {
          @Override
          public void run() {
            try {
              parse(content);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
      }
    }
  }

  private static void parse(String content) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    Document doc = Jsoup.parse(content);
    Elements elems = doc.select("a");
    for (Element elem : elems) {
      if ("下载视频".equals(elem.text())) {
        System.out.println(elem.attr("href") + elem.text());
        HttpGet request = new HttpGet(elem.attr("href"));
        request.setHeader("Accept-Type", "application/octet-stream");
        CloseableHttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        int responseCode = response.getStatusLine().getStatusCode();
        System.out.println("Request Url: " + request.getURI());
        System.out.println("Response Code: " + responseCode);
        InputStream is = entity.getContent();
        String filePath = "/Users/hunnyhu/Downloads/download/" + new Date().getTime() + ".mp4";
        FileOutputStream fos = new FileOutputStream(new File(filePath));
        int inByte;
        while ((inByte = is.read()) != -1) {
          fos.write(inByte);
        }
        is.close();
        fos.close();
      }
    }
  }
}
