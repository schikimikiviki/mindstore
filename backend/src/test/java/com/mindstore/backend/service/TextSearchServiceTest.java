package com.mindstore.backend.service;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.core.search.HitsMetadata;
import org.opensearch.client.opensearch.core.search.TotalHits;
import org.opensearch.client.opensearch.core.search.TotalHitsRelation;


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

        Hit<TextDocument> hit = Hit.of(h -> h
                .source(testText)
                .sort(List.of("someSortValue"))
        );

        TotalHits totalHits = TotalHits.of(t -> t
                .value(1L)
                .relation(TotalHitsRelation.Eq)
        );

        HitsMetadata<TextDocument> hitsMetadata = HitsMetadata.of(h -> h
                .hits(List.of(hit))
                .total(totalHits)
        );

        when(responseMock.hits()).thenReturn(hitsMetadata);
        when(client.search(any(SearchRequest.class), eq(TextDocument.class))).thenReturn(responseMock);

        SearchResultDto<TextDocument> results = textSearchService.findAll("", 10);

        System.out.println(results);

        assertEquals(10, results.getSize());
        assertEquals("Test Title", results.getContent().get(0).getTitle());
    }


}

