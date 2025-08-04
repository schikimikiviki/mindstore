package com.mindstore.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

/**
 * Main java entry point
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {
		ElasticsearchDataAutoConfiguration.class
})
public class BackendApplication {


	/**
	 * Default constructor.
	 */
	public BackendApplication() {
	}

	/**
	 * main function
	 * @param args for command line arguments
	 */
	public static void main(String[] args) {

		System.out.println("starting");

		SpringApplication.run(BackendApplication.class, args);


	}

}
