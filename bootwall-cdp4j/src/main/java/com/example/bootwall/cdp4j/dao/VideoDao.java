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
import org.springframework.transaction.annotation.Transactional;

@Repository
public class VideoDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Transactional
  public void insert(final Video video) {
    final String insert = new StringBuilder() //
        .append("insert into video (") //
        .append("uuid, url, downloaded, type, dateCreated, lastUpdated") //
        .append(") values (") //
        .append("?, ?, ?, ?, now(), now()") //
        .append(") ") //
        .toString();
    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(insert);
        stmt.setString(1, video.getUuid());
        stmt.setString(2, video.getUrl());
        stmt.setString(3, video.getDownloaded());
        stmt.setString(4, video.getType());
        return stmt;
      }
    });
  }

  @Transactional
  public void update(String uuid, String downloaded) {
    final String update = new StringBuilder() //
        .append("update video set downloaded = ?, lastUpdated = now() where uuid = ?") //
        .toString();
    jdbcTemplate.update(update, new Object[] { //
        uuid, //
        downloaded //
    });
  }

  public String getBy(String uuid) {
    String sql = new StringBuilder() //
        .append("select uuid from video where uuid = ? limit 1") //
        .toString();
    List<String> list = jdbcTemplate.query(sql, //
        new Object[] { //
            uuid, //
        }, new SingleColumnRowMapper<String>(String.class));
    if (null == list || list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

}
