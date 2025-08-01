package com.mindstore.backend.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.repository.UserRepository;
import com.mindstore.backend.service.TextIndexService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Component
public class Initializer implements CommandLineRunner {


    private final TextIndexService textIndexService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Initializer(TextIndexService textIndexService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.textIndexService = textIndexService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param args
     * function: initializes the application by adding the textDocuments that are present in the data.json
     * and by adding a test user that can be used in tests
     * note: the first step is deleting all existing entries --> this can be commented out later
     * @throws Exception when the data file is not found or the initialization cannot be done (for whatever reasons)
     */
    @Override
    public void run(String... args) throws Exception {
        try {

           // textIndexService.deleteAll();
            Thread.sleep(2000); // Simulate delay

            // Load JSON data
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/data.json");
            if (inputStream == null) {
                throw new FileNotFoundException("data.json not found in resources");
            }

            // Parse the JSON
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(inputStream));

            // Log the number of entries read
            System.out.println("Read JSON data file with " + array.size() + " entries");

            ObjectMapper mapper = new ObjectMapper();
            int newTextCount = 0;
            int skippedCount = 0;
            int errorCount = 0;

            for (Object entry : array) {
                try {
                    JSONObject jsonEntry = (JSONObject) entry;
                    String title = (String) jsonEntry.get("title");
                    String content_raw = (String) jsonEntry.get("content_raw");
                    String content_html = (String) jsonEntry.get("content_html");
                    JSONArray tagsArray = (JSONArray) jsonEntry.get("tags");
                    JSONArray commandListArray = (JSONArray) jsonEntry.get("commandList");

                    if (title == null || content_raw == null || content_html == null ||
                            tagsArray == null || commandListArray == null || textIndexService.existsByTitle(title)) {
                        System.out.println("Skipping entry with missing fields: " + jsonEntry);
                        skippedCount++;
                        continue; //
                    }

                    // Normalize title
                    String normalizedTitle = title.trim().toLowerCase();

                    // Map JSON to object
                    TextDocument index = new TextDocument();
                    index.setTitle(title);
                    index.setContent_raw(content_raw);
                    index.setContent_html(content_html);

                    List<Category> tags = mapper.readValue(tagsArray.toJSONString(),
                            new TypeReference<List<Category>>(){});
                    index.setTags(tags);

                    List<String> commands = mapper.readValue(commandListArray.toJSONString(),
                            new TypeReference<List<String>>(){});
                    index.setCommandList(commands);

                    // Set timestamps
                    index.setCreatedAt(new Date());
                    index.setUpdatedAt(new Date());

                    // Save to index
                    textIndexService.indexText(index);
                    newTextCount++;

                } catch (Exception e) {
                    System.out.println("Error processing entry: " + entry);
                    e.printStackTrace();
                    errorCount++;
                }
            }

            // Print summary
            System.out.println("Initialization complete:");
            System.out.println(" - Total entries in JSON: " + array.size());
            System.out.println(" - Successfully added: " + newTextCount);
            System.out.println(" - Skipped duplicates: " + skippedCount);
            System.out.println(" - Errors encountered: " + errorCount);

            // Add test user
            User user = new User();
            String email = "test@test.at";
            Optional<User> foundUser = userRepository.findByEmail(email);

            if (foundUser.isEmpty()) {
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode("testPassword"));
                user.setFullName("Test user");
                userRepository.save(user);
                System.out.println("Saving test user.");
            } else {
                System.out.println("User already exists, skipping.");
            }

        } catch (Exception e) {
            System.err.println("Error initializing application: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

