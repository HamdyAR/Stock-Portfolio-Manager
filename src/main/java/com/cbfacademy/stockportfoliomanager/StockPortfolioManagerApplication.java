package com.cbfacademy.stockportfoliomanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StockPortfolioManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPortfolioManagerApplication.class, args);
	}

}
