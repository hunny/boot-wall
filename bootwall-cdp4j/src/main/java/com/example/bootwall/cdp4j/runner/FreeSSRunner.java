package com.example.bootwall.cdp4j.runner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

@Component
public class FreeSSRunner implements CommandLineRunner {

  public static final String IP_PATTERN = //
      "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public void run(String... args) throws Exception {

    Launcher launcher = new Launcher();
    SessionFactory factory = launcher.launch();
    Session session = factory.create();
    session.getCommand().getNetwork().enable();
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
            String ip = tds.get(1).text();
            String port = tds.get(2).text();
            String check = getBy(ip, port);
            if (null == check) {
              List<String> list = new ArrayList<>();
              list.add(ip);
              list.add(port);
              list.add(tds.get(3).text());
              list.add(tds.get(4).text());
              list.add(tds.get(0).text());
              list.add(tds.get(6).text());
              list.add(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " " + tds.get(5).text());
              insert(list);
            }
          }
        }
      }
    }
    System.exit(0);
  }

  public void insert(List<String> list) {
    final String insert = new StringBuilder() //
        .append("insert into freess (") //
        .append("ip, port, method, password, vtm, country, dateCreated, lastUpdated") //
        .append(") values (") //
        .append("?, ?, ?, ?, ?, ?, now(), ?") //
        .append(") ") //
        .toString();
    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(insert);
        for (int i = 0; i < list.size(); i++) {
          stmt.setString(i + 1, list.get(i));
        }
        return stmt;
      }
    });
  }
  
  public String getBy(String ip, String port) {
    String sql = new StringBuilder() //
        .append("select ip from freess where ip = ? and port = ? limit 2") //
        .toString();
    List<String> list = jdbcTemplate.query(sql, //
        new Object[] { //
            ip, //
            port, //
        }, 
        new SingleColumnRowMapper<String>(String.class));
    if (null == list || list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

}
