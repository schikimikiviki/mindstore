package com.mindstore.backend.service;

import com.mindstore.backend.data.entity.Text;
import com.mindstore.backend.data.TextIndex;
import com.mindstore.backend.repository.TextRepository;
import jakarta.transaction.Transactional;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TextIndexService {

    private final TextRepository textRepository;
    private final OpenSearchClient client;

    public TextIndexService(TextRepository textRepository, OpenSearchClient client) {
        this.textRepository = textRepository;
        this.client = client;
    }

    @Transactional
    public void indexAllTexts() {
// index all that bs first
        createIndexIfNotExists();

        // Delete all documents from the index manually using OpenSearchClient
        try {
            client.deleteByQuery(d -> d
                    .index("text-index")
                    .query(q -> q.matchAll(m -> m))
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete all indexed texts", e);
        }

        List<Text> texts = textRepository.findAll();
        for (Text text : texts) {
            indexText(text);
        }
    }

    TextIndex convertToIndex(Text text) {
        TextIndex index = new TextIndex();

        index.setId(text.getId());
        index.setTitle(text.getTitle());
        index.setContent_raw(text.getContent_raw());
        index.setContent_html(text.getContent_html());
        index.setCreatedAt(text.getCreatedAt());
        index.setUpdatedAt(text.getUpdatedAt());
        index.setTags(text.getTags());
        index.setCommandList(text.getCommandList());
        return index;
    }

    public void indexText(Text text) {
        TextIndex index = convertToIndex(text);
        try {
            client.index(i -> i
                    .index("text-index")
                    .id(String.valueOf(index.getId()))
                    .document(index)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to index text", e);
        }
    }

    public void deleteTextIndex(Integer id) {
        try {
            client.delete(d -> d
                    .index("text-index")
                    .id(String.valueOf(id))
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete indexed text", e);
        }
    }

    public void createIndexIfNotExists() {
        try {
            boolean exists = client.indices().exists(e -> e.index("text-index")).value();
            if (!exists) {
                client.indices().create(c -> c.index("text-index"));
                System.out.println("Created index: text-index");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create index", e);
        }
    }

}
