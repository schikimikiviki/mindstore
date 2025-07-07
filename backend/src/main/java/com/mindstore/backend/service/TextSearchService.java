package com.mindstore.backend.service;

import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextSearchService {

    private final OpenSearchClient client;

    public TextSearchService(OpenSearchClient client) {
        this.client = client;
    }

    public SearchResultDto<TextDocument> search(String query, int page, int size) {
        try {
            SearchResponse<TextDocument> response = client.search(s -> s
                            .index("text-index")
                            .from(page * size)
                            .size(size)
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .fields("title", "content_raw")
                                            .query(query)
                                    )
                            ),
                    TextDocument.class
            );

            List<TextDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            long total = response.hits().total().value();

            return new SearchResultDto<>(results, total, page, size);

        } catch (IOException e) {
            throw new RuntimeException("Search failed", e);
        }
    }
}
