package com.test.MySqlConnectorSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * ****** IMPORTENT ******
 * 
 * The Example assumes the schema is already created please see example how to create the schema in : 
 * MySqlConnector\src\MySqlConnector.java 
 * 
 * The spring boot and all the dependencies  are based on pom.xml 
 * 
 * based on mishmash from :
 * https://start.spring.io/ 
 * https://spring.io/guides/gs/accessing-data-mysql/
 * rest mapping : https://www.baeldung.com/spring-requestmapping
 * 
 * Add data :
 * in browser invoke: http://localhost:8080/demo/add?first=mmm&last=bbbb&age=12
 * Select Data 
 * and http://localhost:8080/demo/all
 */


@SpringBootApplication
public class MySqlConnectorSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySqlConnectorSpringBootApplication.class, args);
	}

}
