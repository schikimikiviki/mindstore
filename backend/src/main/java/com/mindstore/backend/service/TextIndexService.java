package com.mindstore.backend.service;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;

import java.io.IOException;
import java.util.List;

public interface TextIndexService {

    /**
     *
     * @param text the TextDocument we want to index
     * function: index a text into our opensearch client
     */
    void indexText(TextDocument text);

    /**
     *
     * @param title string
     * function: check if textDocuments exist with a specific title, used in tests
     * @return true if the title exists, else false
     */
    boolean existsByTitle(String title);

    /**
     * function: deletes all textDocuments that are available
     * @throws IOException when there is an error deleting all entries
     */
    void deleteAll() throws IOException;


}
