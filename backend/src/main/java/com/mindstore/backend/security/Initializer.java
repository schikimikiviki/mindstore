package com.mindstore.backend.security;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.entity.Text;
import com.mindstore.backend.repository.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    TextRepository textRepository;

    @Override
    public void run(String... args) {



        String title = "How to Use Docker with Spring Boot";

        boolean alreadyExists = textRepository.existsByTitle(title);

        if (!alreadyExists) {
            Text t1 = new Text();
            t1.setTitle(title);
            t1.setContent_raw("Here's how to set up Docker for a Spring Boot project...");
            t1.setContent_html("<p>Here's how to set up Docker for a Spring Boot project...</p>");
            t1.setTags(List.of(Category.DOCKER, Category.DEPLOYMENT));
            t1.setCommandList(List.of("docker build -t myapp .", "docker run -p 8080:8080 myapp"));

            textRepository.save(t1);
            System.out.println("Database initialized!");
        } else {
            System.out.println("Database already contains entry with that title.");
        }

    }
}

