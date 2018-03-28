package com.example.bootwall.cdp4j.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DaoConfig {

  @Bean
  public DataSource dataSource() {
    return DataSourceBuilder.create() //
        .driverClassName("com.mysql.jdbc.Driver") //
        .username("root") //
        .password("mysqlpasswd") //
        .url("jdbc:mysql://localhost:4306/topease") //
        .build();
  }
  
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
  
}
