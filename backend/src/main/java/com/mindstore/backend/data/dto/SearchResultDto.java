package com.mindstore.backend.data.dto;


import java.util.List;

public class SearchResultDto<T> {
    private List<T> content;
    private long total;
    private int page;
    private int size;
    private String searchAfter;
    private boolean hasMore;

    public SearchResultDto(List<T> content, long total, int page, int size, String searchAfter, boolean hasMore) {
        this.content = content;
        this.total = total;
        this.page = page;
        this.size = size;
        this.searchAfter = searchAfter;
        this.hasMore = hasMore;
    }

    public void setHasMore(boolean hasMore){
        this.hasMore = hasMore;
    }

    public boolean getHasMore(){
        return hasMore;
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
