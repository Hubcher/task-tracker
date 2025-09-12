package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class MainProjectApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(MainProjectApplication.class, args);
	}

}
