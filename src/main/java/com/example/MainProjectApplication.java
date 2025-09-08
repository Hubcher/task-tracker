package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()// для отключения подключения к БД
public class MainProjectApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(MainProjectApplication.class, args);
	}

}
