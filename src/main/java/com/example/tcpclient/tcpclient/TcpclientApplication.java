package com.example.tcpclient.tcpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TcpclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcpclientApplication.class, args);
	}

}
