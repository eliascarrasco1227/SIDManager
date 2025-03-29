package com.isban.scf.sid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SpringBootApplication
public class SidManagerApplication {
	
	private static final Logger logger = LogManager.getLogger(SidManagerApplication.class);

	/**
	 * This method run a web application that provides a data table with all the documents
	 * of the database even the ones that are not saved yet.
	 * The application has filters in the view (front end) to improve the information searched.
	 * This web has been created with spring boot.
	 *
	 *
	 * @param args An unused variable
	 */
	public static void main(String[] args) {
		System.setProperty("log4j.configurationFile","./../../src/main/resources/log4j2.xml");
		
		logger.info(" ////////////////////////////////////////////////////////////////////// ");
		logger.info(" ------------------------ Application starting ------------------------ ");
		
		SpringApplication.run(SidManagerApplication.class, args);
		
		logger.info(" ------------------------ Application created successfully ------------------------ ");
		logger.debug(" __________________________________________________________________________________ ");
	}

}

