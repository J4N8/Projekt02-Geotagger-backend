package me.j4n8.projekt02backend;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{"localhost"});
        dataSource.setPortNumbers(new int[]{5432});
        dataSource.setDatabaseName("projekt02");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }
    //TODO: Get data from .ENV file
}
