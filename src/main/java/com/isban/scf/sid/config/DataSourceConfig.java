package com.isban.scf.sid.config;

import com.isban.scf.sid.utils.Encryptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DataSourceConfig {
	
	private static final Logger logger = LogManager.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${encrypted.spring.datasource.password}")
    private String encryptedPassword;

    /**
     * This method return a driver that uses spring boot
     * with all the data required for the database connection.
     * It has database url string connection, database username and database password which is decrypted with Encryptor class.
     * 
     * 
     * @return a driver in DataSource format with all information about connection. 
     */
    @Bean
    @Primary
    public DataSource getDataSource() {
    	logger.debug(" --- Creating driver for database connection --- ");
    	
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);

        try {
            String decryptedPassword = Encryptor.decryptMessage(encryptedPassword);
            dataSource.setPassword(decryptedPassword);
        } catch (Exception e) {
        	logger.error(" ERROR while decrypting the database password (check aplication.properties): ", e);
            throw new RuntimeException();
        }

        logger.debug(" --- Driver was successfuly created --- ");
        return dataSource;
    }
}