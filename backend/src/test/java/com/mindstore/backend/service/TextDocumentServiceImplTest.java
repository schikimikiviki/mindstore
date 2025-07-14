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
import org.opensearch.client.opensearch.core.*;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.core.search.HitsMetadata;
import org.opensearch.client.opensearch.core.search.TotalHits;
import org.opensearch.client.opensearch.core.search.TotalHitsRelation;
import org.opensearch.client.opensearch.indices.OpenSearchIndicesClient;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextDocumentServiceImplTest {

    @Mock
    private OpenSearchClient client;

    @InjectMocks
    private TextIndexServiceImpl textIndexService;

    private TextDocument testText;

    @Mock
    private TextSearchService textSearchService;

    @BeforeEach
    void setUp() {
        testText = new TextDocument();
        testText.setTitle("Test Title");
        testText.setContent_raw("Test Content");
        testText.setContent_html("<h1>Test Content</h1>");
        testText.setTags(List.of(Category.DATABASES, Category.CYBERSECURITY));
        testText.setCommandList(List.of("some command", "some other command"));
    }

    @Test
    void shouldIndexText() throws IOException {
        TextDocument text = new TextDocument();
        text.setTitle("My Title");
        textIndexService.indexText(text);

        // verify that the method was called on the mocked obj
        verify(client).index(any(Function.class));

    }

    @Test
    void testIndexText_shouldGenerateIdIfNull() throws IOException {
        TextDocument textDocument = new TextDocument();
        textDocument.setTitle("Test Title");

      // do nothing
        when(client.index(any(Function.class))).thenReturn(null);

        textIndexService.indexText(textDocument);

        assertNotNull(textDocument.getId(), "ID should be generated if null");
        verify(client).index(any(Function.class));
    }

//    @Test
//    void testFindAll_shouldReturnTextList() throws IOException {
//
//        SearchResponse<TextDocument> responseMock = mock(SearchResponse.class);
//        Hit<TextDocument> hit = Hit.of(h -> h.source(testText));
//
//        when(responseMock.hits()).thenReturn(HitsMetadata.of(h -> h.hits(List.of(hit))));
//        when(client.search(any(Function.class), eq(TextDocument.class))).thenReturn(responseMock);
//
//        List<TextDocument> results = textSearchService.findAll();
//
//
//        System.out.println(results);
//
//        assertEquals(1, results.size());
//        assertEquals("Test Title", results.get(0).getTitle());
//    }

    // verify that countTexts() works correcly
    @Test
    void testCountTexts_shouldReturnTotal() throws IOException {

        TotalHits totalHits = TotalHits.of(t -> t.value(42L).relation(TotalHitsRelation.Eq));
        List<Hit<TextDocument>> emptyHits = new ArrayList<>();
        HitsMetadata<TextDocument> hitsMetadata = HitsMetadata.of(h -> h.total(totalHits).hits(emptyHits));
        SearchResponse<TextDocument> responseMock = mock(SearchResponse.class);

        when(responseMock.hits()).thenReturn(hitsMetadata);
        when(client.search(any(Function.class), eq(TextDocument.class))).thenReturn(responseMock);

        long count = textIndexService.countTexts();

        assertEquals(42L, count);
    }

    @Test
    void testExistsByTitle_shouldReturnTrue() throws IOException {

        TotalHits totalHits = TotalHits.of(t -> t.value(1L).relation(TotalHitsRelation.Eq));
        List<Hit<TextDocument>> emptyHits = new ArrayList<>();
        HitsMetadata<TextDocument> hitsMetadata = HitsMetadata.of(h -> h.total(totalHits).hits(emptyHits));
        SearchResponse<TextDocument> responseMock = mock(SearchResponse.class);

        when(responseMock.hits()).thenReturn(hitsMetadata);
        when(client.search(any(Function.class), eq(TextDocument.class))).thenReturn(responseMock);

        boolean exists = textIndexService.existsByTitle("Test Title");

        assertTrue(exists);
    }

    @Test
    void testDeleteAll_shouldCallDeleteByQuery() throws IOException {

        OpenSearchIndicesClient indicesClient = mock(OpenSearchIndicesClient.class);

        when(client.indices()).thenReturn(indicesClient);

        when(indicesClient.exists(any(Function.class))).thenReturn( new BooleanResponse(true));

        when(client.deleteByQuery(any(Function.class))).thenReturn(null);

        textIndexService.deleteAll();

        verify(client, times(1)).deleteByQuery(any(Function.class));
    }











}