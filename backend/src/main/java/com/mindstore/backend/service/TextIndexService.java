package com.mindstore.backend.service;

import com.mindstore.backend.data.entity.Text;
import com.mindstore.backend.data.TextIndex;
import com.mindstore.backend.repository.TextRepository;
import jakarta.transaction.Transactional;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TextIndexService {


    private final OpenSearchClient client;

    public TextIndexService( OpenSearchClient client) {
        this.client = client;
    }

    public void indexText(TextIndex text) {

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


    public List<TextIndex> findAll() {
        try {
            var response = client.search(s -> s
                            .index("text-index")
                            .query(q -> q.matchAll(m -> m))
                            .size(1000),
                    TextIndex.class
            );

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch all indexed texts", e);
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
                    ), TextIndex.class);

            return response.hits().total().value() > 0;
        } catch (IOException e) {
            throw new RuntimeException("Failed to check if title exists", e);
        }
    }

    public void deleteAll() {
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
