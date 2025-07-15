package com.mindstore.backend.service;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.core.search.HitsMetadata;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TextSearchServiceTest {

        @Mock
        private OpenSearchClient client;

        private TextDocument testText;

        @InjectMocks
        private TextSearchService textSearchService;

    /**
     * function: set up a testText
     */
    @BeforeEach
        void setUp() {
            testText = new TextDocument();
            testText.setTitle("Test Title");
            testText.setContent_raw("Test Content");
            testText.setContent_html("<h1>Test Content</h1>");
            testText.setTags(List.of(Category.DATABASES, Category.CYBERSECURITY));
            testText.setCommandList(List.of("some command", "some other command"));
        }


    /**
     *
     * function: check if findAll() returns the correct size after the test text was indexed.
     * Also, check if the title of the first result is equal to the title of the test text
     * @throws IOException when client.search() fails
     */
    @Test
    void testFindAll_shouldReturnTextList() throws IOException {

        SearchResponse<TextDocument> responseMock = mock(SearchResponse.class);
        Hit<TextDocument> hit = Hit.of(h -> h.source(testText));

        when(responseMock.hits()).thenReturn(HitsMetadata.of(h -> h.hits(List.of(hit))));
        when(client.search(any(Function.class), eq(TextDocument.class))).thenReturn(responseMock);

        List<TextDocument> results = textSearchService.findAll();


        System.out.println(results);

        assertEquals(1, results.size());
        assertEquals("Test Title", results.get(0).getTitle());
    }

}

