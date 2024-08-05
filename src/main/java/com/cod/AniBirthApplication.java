package com.cod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // @CreatedDate, @LastModifiedDate를 사용하기 위해 필요
public class AniBirthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AniBirthApplication.class, args);
	}

}
