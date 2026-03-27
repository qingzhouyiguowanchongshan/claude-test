package com.shop.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

/**
 * Parses Railway's DATABASE_URL (postgres://user:pass@host:port/db)
 * into a JDBC datasource. Only active when DATABASE_URL is set.
 * Falls back to application.yml PGHOST/PGPORT vars for local dev.
 */
@Configuration
@ConditionalOnProperty("DATABASE_URL")
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource(@Value("${DATABASE_URL}") String databaseUrl) throws Exception {
        URI uri = new URI(databaseUrl.replaceFirst("^postgres://", "http://")
                                     .replaceFirst("^postgresql://", "http://"));
        String[] userInfo = uri.getUserInfo().split(":", 2);

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath());
        ds.setUsername(userInfo[0]);
        ds.setPassword(userInfo[1]);
        return ds;
    }
}
