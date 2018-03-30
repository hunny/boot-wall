package com.example.bootwall.cdp4j.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FreeSsDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

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
        }, new SingleColumnRowMapper<String>(String.class));
    if (null == list || list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

}
