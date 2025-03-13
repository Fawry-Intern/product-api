package com.fawry.product_api.config;

import com.fawry.product_api.config.properties.DatasourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DBConfig {

    private final DatasourceProperties properties;

    public DBConfig(DatasourceProperties properties) {
        this.properties = properties;
    }

    @Bean("datasource_db")
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(properties.getDriver());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return dataSource;
    }

}
