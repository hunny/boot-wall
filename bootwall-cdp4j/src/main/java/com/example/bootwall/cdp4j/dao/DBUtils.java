package com.example.bootwall.cdp4j.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/topease";
  static final String USER = "topease";
  static final String PASS = "topease";

  public static Connection getConnection() {
    Connection dbConnection = null;
    try {
      Class.forName(JDBC_DRIVER);
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
    try {
      System.out.println("Connecting to database...");
      dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);
      return dbConnection;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return dbConnection;

  }

  public static void db(Opt opt) throws Exception {
    if (null == opt) {
      return;
    }
    Connection conn = getConnection();
    // STEP 4: Execute a query
    System.out.println("Creating statement...");
    Statement stmt = conn.createStatement();
    opt.run(conn, stmt);
    close(stmt);
    close(conn);
  }

  public static void close(ResultSet rs) {
    try {
      rs.close();
    } catch (Exception e) {
    }
  }
  
  public static void close(Statement stmt) {
    try {
      if (stmt != null) {
        stmt.close();
      }
    } catch (Exception se2) {
    } // nothing we can do
  }
  
  public static void close(PreparedStatement stmt) {
    try {
      if (stmt != null) {
        stmt.close();
      }
    } catch (Exception se2) {
    } // nothing we can do
  }

  public static void db(OptConn optConn) throws Exception {
    if (null == optConn) {
      return;
    }
    Connection conn = getConnection();
    // STEP 4: Execute a query
    optConn.run(conn);
    close(conn);
  }

  public static void close(Connection conn) {
    try {
      conn.close();
    } catch (Exception se) {
      se.printStackTrace();
    } // end finally try
  }

  public interface Opt {
    void run(Connection conn, Statement stmt);
  }

  public interface OptConn {
    void run(Connection conn);
  }

}
