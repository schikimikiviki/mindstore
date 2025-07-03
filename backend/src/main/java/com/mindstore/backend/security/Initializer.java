package com.mindstore.backend.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextIndex;
import com.mindstore.backend.service.TextIndexService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.UUID;



@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private TextIndexService textIndexService;

    @Override
    public void run(String... args) throws Exception {
        textIndexService.deleteAll();

        Thread.sleep(5000);


        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(new FileReader("src/data/data.json"));
        System.out.println("Read JSON data file with " + array.size() + " entries");

        ObjectMapper mapper = new ObjectMapper();
        int newTextCount = 0;
        int skippedCount = 0;
        int errorCount = 0;

        for (Object entry : array) {
            try {
                JSONObject jsonEntry = (JSONObject) entry;

                // Validate required fields
                String title = (String) jsonEntry.get("title");
                String content_raw = (String) jsonEntry.get("content_raw");
                String content_html = (String) jsonEntry.get("content_html");
                JSONArray tagsArray = (JSONArray) jsonEntry.get("tags");
                JSONArray commandListArray = (JSONArray) jsonEntry.get("commandList");

                if (title == null || content_raw == null || content_html == null ||
                        tagsArray == null || commandListArray == null) {
                    System.out.println("Skipping entry with missing fields: " + jsonEntry);
                    skippedCount++;
                    continue;
                }

                // Normalize title for comparison
                String normalizedTitle = title.trim().toLowerCase();
//                if (textIndexService.existsByTitle(normalizedTitle)) {
//                    System.out.println("Skipping duplicate title: " + title);
//                    skippedCount++;
//                    continue;
//                }

                // Create and populate the index
                TextIndex index = new TextIndex();
                index.setTitle(title);
                index.setContent_raw(content_raw);
                index.setContent_html(content_html);

                // Handle tags - assuming Category has a name field
                List<Category> tags = mapper.readValue(tagsArray.toJSONString(),
                        new TypeReference<List<Category>>(){});
                index.setTags(tags);

                // Handle commands
                List<String> commands = mapper.readValue(commandListArray.toJSONString(),
                        new TypeReference<List<String>>(){});
                index.setCommandList(commands);

                // Set timestamps
                index.setCreatedAt(new Date());
                index.setUpdatedAt(new Date());

                // Save
                textIndexService.indexText(index);
                newTextCount++;

            } catch (Exception e) {
                System.out.println("Error processing entry: " + entry);
                e.printStackTrace();
                errorCount++;
            }
        }

        System.out.println("Initialization complete:");
        System.out.println(" - Total entries in JSON: " + array.size());
        System.out.println(" - Successfully added: " + newTextCount);
        System.out.println(" - Skipped duplicates: " + skippedCount);
        System.out.println(" - Errors encountered: " + errorCount);
    }
}

