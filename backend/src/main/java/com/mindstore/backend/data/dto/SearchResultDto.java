package com.mindstore.backend.data.dto;


import java.util.List;

public class SearchResultDto<T> {
    private List<T> content;
    private long total;
    private int page;
    private int size;

    public SearchResultDto(List<T> content, long total, int page, int size) {
        this.content = content;
        this.total = total;
        this.page = page;
        this.size = size;
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
