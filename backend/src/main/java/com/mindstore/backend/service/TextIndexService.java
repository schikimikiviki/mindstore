package com.mindstore.backend.service;

import com.mindstore.backend.data.TextDocument;

import java.io.IOException;
import java.util.List;

public interface TextIndexService {

    void indexText(TextDocument text);
    List<TextDocument> findAll();
    long countTexts();
    boolean existsByTitle(String title);
    void deleteAll() throws IOException;


}
