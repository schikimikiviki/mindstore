package com.mindstore.backend.service;

import com.mindstore.backend.data.TextIndex;

import java.util.List;

public interface TextIndexService {

    void indexText(TextIndex text);
    List<TextIndex> findAll();
    long countTexts();
    boolean existsByTitle(String title);
    void deleteAll();


}
