package com.mindstore.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
		ElasticsearchDataAutoConfiguration.class
})
public class BackendApplication {

	public static void main(String[] args) {

		System.out.println("starting");

//		Dotenv dotenv = Dotenv.load();
//		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
//		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
//		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
//		//System.out.println("Read: " +  dotenv.get("SPRING_DATASOURCE_USERNAME") +  " and: " + dotenv.get("SPRING_DATASOURCE_PASSWORD"));


		SpringApplication.run(BackendApplication.class, args);


	}

}
