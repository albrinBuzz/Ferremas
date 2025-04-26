package com.ferremas;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication

@ComponentScan(basePackages = "com.ferremas;")
public class FerremasApplication {

	public static void main(String[] args) {
		var context= SpringApplication.run(FerremasApplication.class, args);




	}

}
