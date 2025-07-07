package com.mindstore.backend.service;

import com.mindstore.backend.data.TextDocument;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TextIndexServiceImpl implements TextIndexService{


    private final OpenSearchClient client;

    public TextIndexServiceImpl(OpenSearchClient client) {
        this.client = client;
    }

    public void indexText(TextDocument text) {

        try {

            if (text.getId() == null) {
                text.setId(Math.abs(UUID.randomUUID().hashCode()));
            }

            client.index(i -> i
                    .index("text-index")
                    .id(String.valueOf(text.getId()))
                    .document(text)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to index text", e);
        }
    }


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


    public long countTexts() {
        try {
            var response = client.search(s -> s
                            .index("text-index")
                            .query(q -> q.matchAll(m -> m))
                            .size(0), // Don't fetch actual documents
                    TextDocument.class
            );

            return response.hits().total().value();

        } catch (IOException e) {
            throw new RuntimeException("Failed to count indexed texts", e);
        }
    }



    public boolean existsByTitle(String title) {
        try {
            var response = client.search(s -> s
                    .index("text-index")
                    .query(q -> q
                            .match(m -> m
                                    .field("title")
                                    .query(FieldValue.of(title))
                            )
                    ), TextDocument.class);

            return response.hits().total().value() > 0;
        } catch (IOException e) {
            throw new RuntimeException("Failed to check if title exists", e);
        }
    }

    public void deleteAll() throws IOException {

        if (!client.indices().exists(b -> b.index("text-index")).value()) {
            client.indices().create(c -> c.index("text-index"));
        }

        try {
            client.deleteByQuery(d -> d
                    .index("text-index")
                    .query(q -> q.matchAll(m -> m))
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete all documents from text-index", e);
        }
    }



}
