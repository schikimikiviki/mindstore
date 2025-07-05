package com.mindstore.backend.service;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch.core.*;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.core.search.HitsMetadata;
import org.opensearch.client.opensearch.core.search.TotalHits;
import org.opensearch.client.opensearch.core.search.TotalHitsRelation;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextIndexServiceImplTest {

    @Mock
    private OpenSearchClient client;

    @InjectMocks
    private TextIndexServiceImpl textIndexService;

    private TextIndex testText;

    @BeforeEach
    void setUp() {
        testText = new TextIndex();
        testText.setTitle("Test Title");
        testText.setContent_raw("Test Content");
        testText.setContent_html("<h1>Test Content</h1>");
        testText.setTags(List.of(Category.DATABASES, Category.CYBERSECURITY));
        testText.setCommandList(List.of("some command", "some other command"));
    }

    @Test
    void shouldIndexText() throws IOException {
        TextIndex text = new TextIndex();
        text.setTitle("My Title");
        textIndexService.indexText(text);

        // verify that the method was called on the mocked obj
        verify(client).index(any(Function.class));

    }

    @Test
    void testIndexText_shouldGenerateIdIfNull() throws IOException {
        TextIndex textIndex = new TextIndex();
        textIndex.setTitle("Test Title");

        OpenSearchClient clientMock = mock(OpenSearchClient.class);
        TextIndexServiceImpl service = new TextIndexServiceImpl(clientMock);

      // do nothing
        when(clientMock.index(any(Function.class))).thenReturn(null);

        service.indexText(textIndex);

        assertNotNull(textIndex.getId(), "ID should be generated if null");
        verify(clientMock).index(any(Function.class));
    }

    @Test
    void testFindAll_shouldReturnTextList() throws IOException {
        OpenSearchClient clientMock = mock(OpenSearchClient.class);
        TextIndexServiceImpl service = new TextIndexServiceImpl(clientMock);

        SearchResponse<TextIndex> responseMock = mock(SearchResponse.class);
        Hit<TextIndex> hit = Hit.of(h -> h.source(testText));

        when(responseMock.hits()).thenReturn(HitsMetadata.of(h -> h.hits(List.of(hit))));
        when(clientMock.search(any(Function.class), eq(TextIndex.class))).thenReturn(responseMock);

        List<TextIndex> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("Test Title", results.get(0).getTitle());
    }

    // verify that countTexts() works correcly
    @Test
    void testCountTexts_shouldReturnTotal() throws IOException {
        OpenSearchClient clientMock = mock(OpenSearchClient.class);
        TextIndexServiceImpl service = new TextIndexServiceImpl(clientMock);

        TotalHits totalHits = TotalHits.of(t -> t.value(42L).relation(TotalHitsRelation.Eq));
        List<Hit<TextIndex>> emptyHits = new ArrayList<>();
        HitsMetadata<TextIndex> hitsMetadata = HitsMetadata.of(h -> h.total(totalHits).hits(emptyHits));
        SearchResponse<TextIndex> responseMock = mock(SearchResponse.class);

        when(responseMock.hits()).thenReturn(hitsMetadata);
        when(clientMock.search(any(Function.class), eq(TextIndex.class))).thenReturn(responseMock);

        long count = service.countTexts();

        assertEquals(42L, count);
    }

    @Test
    void testExistsByTitle_shouldReturnTrue() throws IOException {

        TotalHits totalHits = TotalHits.of(t -> t.value(1L).relation(TotalHitsRelation.Eq));
        List<Hit<TextIndex>> emptyHits = new ArrayList<>();
        HitsMetadata<TextIndex> hitsMetadata = HitsMetadata.of(h -> h.total(totalHits).hits(emptyHits));
        SearchResponse<TextIndex> responseMock = mock(SearchResponse.class);

        when(responseMock.hits()).thenReturn(hitsMetadata);
        when(client.search(any(Function.class), eq(TextIndex.class))).thenReturn(responseMock);

        boolean exists = textIndexService.existsByTitle("Test Title");

        assertTrue(exists);
    }

    @Test
    void testDeleteAll_shouldCallDeleteByQuery() throws IOException {
        when(client.deleteByQuery(any(Function.class))).thenReturn(null);

        textIndexService.deleteAll();

        verify(client, times(1)).deleteByQuery(any(Function.class));
    }











}