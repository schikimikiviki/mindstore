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

    /**
     * function: sets up a test text to work with
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
     * function: check if the testText is indexed correctly
     * @throws IOException when the .index() fails
     */
    @Test
    void shouldIndexText() throws IOException {

        textIndexService.indexText(testText);

        // verify that the method was called on the mocked obj
        verify(client).index(any(Function.class));

    }

    /**
     *
     * function: check if the text id is generated correctly if none is provided
     * @throws IOException when index() fails
     */
    @Test
    void testIndexText_shouldGenerateIdIfNull() throws IOException {

      // do nothing
        when(client.index(any(Function.class))).thenReturn(null);

        textIndexService.indexText(testText);

        assertNotNull(testText.getId(), "ID should be generated if null");
        verify(client).index(any(Function.class));
    }

    /**
     *
     * function: checks if the existsByTitle() returns true after a text was indexed
     * @throws IOException if the client.search() fails
     */
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

    /**
     *
     * function: test if deleteAll() is correctly executed
     * @throws IOException when client.deleteAll(), deleteByQuery() or indicesClient.exists() fails
     */
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