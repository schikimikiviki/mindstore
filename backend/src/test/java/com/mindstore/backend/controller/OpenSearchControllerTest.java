package com.mindstore.backend.controller;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import com.mindstore.backend.service.TextIndexService;
import com.mindstore.backend.service.TextSearchService;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.core.search.HitsMetadata;
import org.opensearch.client.opensearch.core.search.TotalHits;
import org.opensearch.client.opensearch.core.search.TotalHitsRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class OpenSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TextIndexService textIndexService;

    @MockitoBean
    private TextSearchService textSearchService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void shouldReturnTextList() throws Exception {

        TextDocument textDocumentOne = new TextDocument();
        textDocumentOne.setTitle("Some random title");
        textDocumentOne.setContent_html("<h1>Soem content</h1>");
        textDocumentOne.setContent_raw("Some content");
        textDocumentOne.setCommandList(List.of("sudo rm -rf", "sudo mkdir"));
        textDocumentOne.setTags(List.of(Category.DATABASES, Category.AI));

        TextDocument textDocumentTwo = new TextDocument();
        textDocumentTwo.setTitle("Some random title 2");
        textDocumentTwo.setContent_html("<h1>Soem content2</h1>");
        textDocumentTwo.setContent_raw("Some content2");
        textDocumentTwo.setCommandList(List.of("sudo rm -rf", "sudo mkdir"));
        textDocumentTwo.setTags(List.of(Category.DOCKER, Category.LINUX));

        List<TextDocument> mockTexts = List.of(textDocumentOne, textDocumentTwo);

        when(textSearchService.findAll()).thenReturn(mockTexts);

        String response = mockMvc.perform(get("/text-index/all"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println("Actual response: " + response);


        mockMvc.perform(get("/text-index/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Some random title"))
                .andExpect(jsonPath("$.content[1].title").value("Some random title 2"));

    }


    @Test
    void testCountTexts_shouldReturnTotal() throws IOException, ParseException {

            // we want there to be as many entries as in the JSON data file

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/data.json");

            if (inputStream == null) {
                throw new FileNotFoundException("data.json not found in resources");
            }

            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(inputStream));

            Long expectedDataSize = (long) array.size();

            List<TextDocument> mockDocs = new ArrayList<>();

            for (int i = 0; i < expectedDataSize; i++) {
                TextDocument doc = new TextDocument();
                doc.setTitle("Mock " + i);
                mockDocs.add(doc);
            }

            when(textSearchService.findAll()).thenReturn(mockDocs);

            List<TextDocument> docs = textSearchService.findAll();
            SearchResultDto<TextDocument> searchResult = new SearchResultDto<>(docs, docs.size(), 0, docs.size());

            long total = searchResult.getTotal();

            assertEquals(expectedDataSize, total);

    }

    @Test
    void testCountTexts_callEndpoint() throws Exception {

        // we want there to be as many entries as in the JSON data file

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/data.json");

        if (inputStream == null) {
            throw new FileNotFoundException("data.json not found in resources");
        }

        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(new InputStreamReader(inputStream));

        Long expectedDataSize = (long) array.size();

        List<TextDocument> mockDocs = new ArrayList<>();

        for (int i = 0; i < expectedDataSize; i++) {
            TextDocument doc = new TextDocument();
            doc.setTitle("Mock " + i);
            mockDocs.add(doc);
        }

        when(textSearchService.findAll()).thenReturn(mockDocs);


        mockMvc.perform(get("/text-index/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(expectedDataSize));


    }




}
