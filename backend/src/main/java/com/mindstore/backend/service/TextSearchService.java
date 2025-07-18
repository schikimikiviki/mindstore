package com.mindstore.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.query_dsl.TextQueryType;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public SearchResultDto<TextDocument> search(String query, int page, int size, String searchAfter) {
        try {
            SearchResponse<TextDocument> response = client.search(s -> {
                SearchRequest.Builder builder = s
                        .index("text-index")
                        .size(size)
                        .sort(sort -> sort
                                .field(f -> f
                                        .field("createdAt")
                                        .order(SortOrder.Desc)
                                )
                        )
                        .query(q -> q
                                .multiMatch(mm -> mm
                                        .query(query)
                                        .fields("title.autocomplete", "content_raw.autocomplete")
                                        .type(TextQueryType.BoolPrefix)
                                )
                        );

                // Apply search_after if provided
                if (searchAfter != null && !searchAfter.isEmpty()) {
                    builder.searchAfter(List.of(searchAfter));
                }

                return builder;
            }, TextDocument.class);

            List<Hit<TextDocument>> hits = response.hits().hits();

            List<TextDocument> results = hits.stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            long total = response.hits().total().value();

            // Extract the sort value from the last hit for next request
            String nextSearchAfter = hits.isEmpty()
                    ? null
                    : hits.get(hits.size() - 1).sort().get(0).toString(); // Assuming sort on one field

            boolean hasMore = hits.size() == size;

            return new SearchResultDto<>(results, total, page, size, nextSearchAfter, hasMore);


        } catch (IOException e) {
            throw new RuntimeException("Search failed", e);
        }
    }


    /**
     *
     * @param prefix - String that we are searching for
     * @return List of titles that match for that string
     * @throws IOException when search fails
     */
    public List<String> autocomplete(String prefix) throws IOException {
        SearchResponse<TextDocument> response = client.search(s -> s
                .index("text-index")
                .size(5)
                .query(q -> q
                        .multiMatch(mm -> mm
                                .query(prefix)
                                .fields("title.autocomplete", "content_raw.autocomplete")
                                .type(TextQueryType.BoolPrefix)
                        )
                ), TextDocument.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .flatMap(doc -> Stream.of(doc.getTitle(), doc.getContent_raw()))
                .filter(Objects::nonNull)
                .flatMap(text -> Arrays.stream(text.split("\\W+")))
                .map(String::toLowerCase) // normalize to lowercase
                .filter(word -> word.startsWith(prefix.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());

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
     * @param searchAfter - searchAfter string
     * @param size - size of the results
     * function: service method that returns all available textDocuments
     * @return list of documents
     */
    public SearchResultDto<TextDocument> findAll(String searchAfter, int size) {
        try {
            SearchRequest.Builder searchBuilder = new SearchRequest.Builder()
                    .index("text-index")
                    .size(size)
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("createdAt")
                                    .order(SortOrder.Desc)
                            )
                    );

            // If searchAfter is present, set it
            if (searchAfter != null && !searchAfter.isEmpty()) {
                searchBuilder.searchAfter(List.of(searchAfter));
            }

            searchBuilder.query(q -> q.matchAll(m -> m));

            SearchResponse<TextDocument> response = client.search(searchBuilder.build(), TextDocument.class);

            List<Hit<TextDocument>> hits = response.hits().hits();

            List<TextDocument> docs = hits.stream()
                    .map(Hit::source)
                    .toList();

            String nextSearchAfter = hits.isEmpty()
                    ? null
                    : (hits.get(hits.size() - 1).sort().get(0));

            boolean hasMore = hits.size() == size;

            return new SearchResultDto<>(docs, response.hits().total().value(), 0, size, nextSearchAfter, hasMore);

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch indexed texts", e);
        }
    }


    /**
     *
     * @param searchAfter - searchAfter string
     * @param size - size of the results
     * @param categories list of category enum strings
     * @return a List with TextDocuments that match that query
     */
    public SearchResultDto<TextDocument> findAllWithTags(List<String> categories, String searchAfter, int size) {

        try {
            var response = client.search(s -> {
                var builder = s
                        .index("text-index")
                        .size(size)
                        .sort(sort -> sort
                                .field(f -> f
                                        .field("createdAt")
                                        .order(SortOrder.Desc)
                                )
                        )
                        .query(q -> q
                                    .terms(t -> t
                                            .field("tags.keyword")
                                            .terms(tt -> tt.value(
                                                    categories.stream()
                                                            .map(FieldValue::of)
                                                            .toList()
                                            ))
                                    )
                            );

                // If a searchAfter is provided, use it
                if (searchAfter != null && !searchAfter.isEmpty()) {
                    builder.searchAfter(List.of(searchAfter));
                }

                return builder;
            }, TextDocument.class);

            List<Hit<TextDocument>> hits = response.hits().hits();

            List<TextDocument> docs = hits.stream()
                    .map(Hit::source)
                    .toList();

            // Get the sort value of the last hit for the next request
            String nextSearchAfter = hits.isEmpty()
                    ? null
                    : hits.get(hits.size() - 1).sort().get(0);

            boolean hasMore = hits.size() == size;

            return new SearchResultDto<>(docs, response.hits().total().value(), 0, size, nextSearchAfter, hasMore);


        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch all indexed texts", e);
        }
    }
}
