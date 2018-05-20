package com.example.bootwall.cdp4j.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
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
        .append("uuid, url, downloaded, type, source, dateCreated, lastUpdated") //
        .append(") values (") //
        .append("?, ?, ?, ?, ?, now(), now()") //
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
        stmt.setString(5, video.getSource());
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
        downloaded, //
        uuid, //
    });
  }
  
  @Transactional
  public void update(String uuid, String downloaded, String text) {
    final String update = new StringBuilder() //
        .append("update video set downloaded = ?, text = ?, lastUpdated = now() where uuid = ?") //
        .toString();
    jdbcTemplate.update(update, new Object[] { //
        downloaded, //
        text, //
        uuid, //
    });
  }
  
  @Transactional
  public void updateText(String uuid, String text) {
    final String update = new StringBuilder() //
        .append("update video set text = ?, lastUpdated = now() where uuid = ?") //
        .toString();
    jdbcTemplate.update(update, new Object[] { //
        text, //
        uuid, //
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

  public List<Map<String, String>> listBy(int limit) {
    String sql = new StringBuilder() //
        .append("select uuid, source as url from video where downloaded is null order by dateCreated desc limit ?") //
        .toString();
    List<Map<String, String>> list = jdbcTemplate.query(sql, //
        new Object[] { //
            limit, //
        }, //
        new RowMapper<Map<String, String>>() {
          @Override
          public Map<String, String> mapRow(ResultSet rs, int arg1) throws SQLException {
            Map<String, String> result = new HashMap<>();
            result.put("uuid", rs.getString("uuid"));
            result.put("url", rs.getString("url"));
            return result;
          }
        });
    return list;
  }
  
  public List<Map<String, String>> listErrorBy(int limit) {
    String sql = new StringBuilder() //
        .append("select uuid, source as url from video where downloaded = 'ERROR' order by dateCreated desc limit ?") //
        .toString();
    List<Map<String, String>> list = jdbcTemplate.query(sql, //
        new Object[] { //
            limit, //
        }, //
        new RowMapper<Map<String, String>>() {
          @Override
          public Map<String, String> mapRow(ResultSet rs, int arg1) throws SQLException {
            Map<String, String> result = new HashMap<>();
            result.put("uuid", rs.getString("uuid"));
            result.put("url", rs.getString("url"));
            return result;
          }
        });
    return list;
  }
  
  public List<Map<String, String>> listTextBy(int limit) {
    String sql = new StringBuilder() //
        .append("select uuid, source as url from video where downloaded = 'OK' and text is null order by dateCreated desc limit ?") //
        .toString();
    List<Map<String, String>> list = jdbcTemplate.query(sql, //
        new Object[] { //
            limit, //
        }, //
        new RowMapper<Map<String, String>>() {
          @Override
          public Map<String, String> mapRow(ResultSet rs, int arg1) throws SQLException {
            Map<String, String> result = new HashMap<>();
            result.put("uuid", rs.getString("uuid"));
            result.put("url", rs.getString("url"));
            return result;
          }
        });
    return list;
  }

}
