package com.phoenix.filter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.phoenix.filter.manager.mapper")
public class FilterApplication {
	public static void main(String[] args) {
		SpringApplication.run(FilterApplication.class, args);
	}

}
