package com.mindstore.backend.data.dto;

import java.util.List;
import java.util.Map;

public class SearchHitDto<T> {
    private T document;
    private Map<String, List<String>> highlights;


    public SearchHitDto(T document, Map<String, List<String>> highlights) {
        this.document = document;
        this.highlights = highlights;
    }

    public T getDocument() {
        return document;
    }

    public void setDocument(T document) {
        this.document = document;
    }

    public Map<String, List<String>> getHighlights() {
        return highlights;
    }

    public void setHighlights(Map<String, List<String>> highlights) {
        this.highlights = highlights;
    }
}
