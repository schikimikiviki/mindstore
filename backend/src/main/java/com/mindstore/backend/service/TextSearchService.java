package com.mindstore.backend.service;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
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

    /**
     *
     * @param query the string we are searching for
     * @param page param used for pagination, page number
     * @param size defines how many results are returned

     * function: search the available TextDocuments for a specific string
     *
     * @return a SearchResultDto with TextDocuments that match the query
     */
    public SearchResultDto<TextDocument> search(String query, int page, int size) {
        try {
            SearchResponse<TextDocument> response = client.search(s -> s
                            .index("text-index")
                            .from(page * size)
                            .size(size)
                            .query(q -> q
//                                    .multiMatch(m -> m
//                                            .fields("title", "content_raw")
//                                            .query(query)
//                                    )
                                            .queryString(qs -> qs
                                                    .query(query + "*") // wildcard for fuzzy matching, eg. "kil" should deliver the same results as "kill"
                                                    .fields("title", "content_raw")
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

    /**
     *
     * function: returns all available values of Category enum
     * @return array of categories
     */
    public Category[] findTags(){
        return  Category.values();
    }

    /**
     *
     * function: service method that returns all available textDocuments
     * @return list of documents
     */
    public List<TextDocument> findAll() {
        try {
            var response = client.search(s -> s
                            .index("text-index")
                            .query(q -> q.matchAll(m -> m))
                            .size(1000),
                    TextDocument.class
            );

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch all indexed texts", e);
        }
    }

    /**
     *
     * @param categories list of category enum strings
     * @return a List with TextDocuments that match that query
     */
    public List<TextDocument> findAllWithTags(List<String> categories) {
        try {
            var response = client.search(s -> s
                            .index("text-index")
                            .query(q -> q
                                    .terms(t -> t
                                            .field("tags.keyword")
                                            .terms(tt -> tt.value(
                                                    categories.stream()
                                                            .map(FieldValue::of)
                                                            .toList()
                                            ))
                                    )
                            )
                            .size(1000),
                    TextDocument.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch all indexed texts", e);
        }
    }
}
