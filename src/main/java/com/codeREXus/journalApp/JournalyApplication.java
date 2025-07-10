package com.codeREXus.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JournalyApplication{

	public static void main(String[] args) {
		SpringApplication.run(JournalyApplication.class, args);
	}

	@Bean
	public PlatformTransactionManager transactionManager (MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}
}
