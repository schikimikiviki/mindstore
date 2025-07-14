package com.mindstore.backend.service;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;

import java.io.IOException;
import java.util.List;

public interface TextIndexService {

    void indexText(TextDocument text);
    boolean existsByTitle(String title);
    void deleteAll() throws IOException;


}
