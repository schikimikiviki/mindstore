package com.mindstore.backend.data.dto;

import java.util.List;
import java.util.Map;

/**
 * SearchHit DTO wrapper class, used to return SeachResults with highlighted results
 * @param <T> any SearchResult
 */
public class SearchHitDto<T> {
    private T document;
    private Map<String, List<String>> highlights;

    /**
     *  Search Hit DTO
     * @param document that is wrapped
     * @param highlights List of Strings that is highlighted
     */
    public SearchHitDto(T document, Map<String, List<String>> highlights) {
        this.document = document;
        this.highlights = highlights;
    }

    /**
     * default getter
     * @return T document
     */
    public T getDocument() {
        return document;
    }

    /**
     * default setter
     * @param document anything that is wrapped
     */
    public void setDocument(T document) {
        this.document = document;
    }

    /**
     * default getter
     * @return hightlight strings
     */
    public Map<String, List<String>> getHighlights() {
        return highlights;
    }

    /**
     * default setter
     * @param highlights the Strings that are highlighted
     */
    public void setHighlights(Map<String, List<String>> highlights) {
        this.highlights = highlights;
    }
}
