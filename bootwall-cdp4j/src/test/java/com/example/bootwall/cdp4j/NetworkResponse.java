package com.example.bootwall.cdp4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class NetworkResponse {

  public static final String IP_PATTERN = //
      "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";

  public static void main(String[] args) {

    Launcher launcher = new Launcher();
    SessionFactory factory = launcher.launch();
    Session session = factory.create();
    session.getCommand().getNetwork().enable();
    // Set<String> finished = new HashSet<>();
    // session.addEventListener((e, d) -> {
    // if (Events.NetworkLoadingFinished.equals(e)) {
    // LoadingFinished lf = (LoadingFinished) d;
    // finished.add(lf.getRequestId());
    // }
    // if (Events.NetworkResponseReceived.equals(e)) {
    // ResponseReceived rr = (ResponseReceived) d;
    // Response response = rr.getResponse();
    // System.out.println("----------------------------------------");
    // System.out.println("URL : " + response.getUrl());
    // System.out.println("Status : HTTP " + response.getStatus().intValue() + "
    // " + response.getStatusText());
    // System.out.println("Mime Type : " + response.getMimeType());
    // System.out.println("----------------------------------------");
    // if (finished.contains(rr.getRequestId())) {
    // GetResponseBodyResult rb =
    // session.getCommand().getNetwork().getResponseBody(rr.getRequestId());
    // if (rb != null) {
    // String body = rb.getBody();
    // System.out.println("Content : " + body.substring(0, body.length() > 100 ?
    // 100 : body.length()));
    // }
    // }
    // }
    // });
    session.navigate(args[0]);

    try {
      session.waitDocumentReady(30 * 1000);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
    if (session.isDomReady()) {
      System.err.println("===========================>");
      Document doc = Jsoup.parse(session.getContent());
      Elements elems = doc.select("table");
      for (Element elem : elems) {
        System.out.println("===========================");
        Elements trs = elem.select("tr[role=\"row\"][class]");
        for (Element tr : trs) {
          Elements tds = tr.select("td");
          if (tds.size() >= 2 //
              && tds.get(1).text() //
                  .matches(IP_PATTERN)) {
            System.out.println(tds.html());
          }
        }
      }
    }
    System.exit(0);
  }
}
