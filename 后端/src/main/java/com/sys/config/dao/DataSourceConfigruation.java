package com.sys.config.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @author sys
 */

@Configuration
@MapperScan("com.sys.dao")
public class DataSourceConfigruation {

    @Value("${spring.datasource.driver-class-name}")
    private String jdbcDriver;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;


    @Bean(name = "dataSource")
    public DataSource createDataSource(DataSourceProperties properties) {

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(jdbcDriver);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);

        config.setMaximumPoolSize(5);
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("HikariCPDB");
        config.setMaxLifetime(1765000);
        config.setAutoCommit(false);
        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }
}
