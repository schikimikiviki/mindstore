package com.mindstore.backend.service;

import com.mindstore.backend.data.TextDocument;
import java.io.IOException;

/**
 * interface for managing text documents
 */
public interface TextIndexService {

    /**
     * function: index a text into our opensearch client
     * @param text the TextDocument we want to index
     */
    void indexText(TextDocument text);

    /**
     * function: check if textDocuments exist with a specific title, used in tests
     * @param title string
     * @return true if the title exists, else false
     */
    boolean existsByTitle(String title);

    /**
     * function: deletes all textDocuments that are available
     * @throws IOException when there is an error deleting all entries
     */
    void deleteAll() throws IOException;

    /**
     *
     * function for deleting text documents
     * @param id - the textDocument id to delete
     * @throws IOException when deletion fails
     */
    void delete(Integer id) throws IOException;
}
