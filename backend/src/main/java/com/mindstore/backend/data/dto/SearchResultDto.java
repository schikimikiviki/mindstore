package com.mindstore.backend.data.dto;


import java.util.List;

public class SearchResultDto<T> {
    private List<T> content;
    private long total;
    private int page;
    private int size;
    private String searchAfter;

    public SearchResultDto(List<T> content, long total, int page, int size, String searchAfter) {
        this.content = content;
        this.total = total;
        this.page = page;
        this.size = size;
        this.searchAfter = searchAfter;
    }

    public String getSearchAfter(){
        return searchAfter;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
