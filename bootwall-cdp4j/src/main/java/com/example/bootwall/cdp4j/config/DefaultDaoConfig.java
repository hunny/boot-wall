package com.example.bootwall.cdp4j.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnBean(DefaultDaoConfigValue.class)
public class DefaultDaoConfig {
  
  @Bean
  public DataSource dataSource(DefaultDaoConfigValue value) {
    return DataSourceBuilder.create() //
        .driverClassName(value.getDriverClassName()) //
        .username(value.getUsername()) //
        .password(value.getPassword()) //
        .url(value.getUrl()) //
        .build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

}
