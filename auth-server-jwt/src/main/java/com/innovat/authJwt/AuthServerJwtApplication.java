package com.innovat.authJwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients()
public class AuthServerJwtApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServerJwtApplication.class, args);
	}

}
