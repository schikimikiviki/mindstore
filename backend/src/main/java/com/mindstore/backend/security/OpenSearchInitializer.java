package com.mindstore.backend.security;

import com.mindstore.backend.service.TextIndexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OpenSearchInitializer implements CommandLineRunner {

    private final TextIndexService textIndexService;

    public OpenSearchInitializer(TextIndexService textIndexService) {
        this.textIndexService = textIndexService;
    }

    @Override
    public void run(String... args) throws Exception {
        textIndexService.indexAllTexts();
    }
}