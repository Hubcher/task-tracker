package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})// для отключения подключения к БД
public class MainProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainProjectApplication.class, args);
	}

}
