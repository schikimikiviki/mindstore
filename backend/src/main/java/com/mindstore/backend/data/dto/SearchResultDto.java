package com.mindstore.backend.data.dto;


import java.util.List;

/**
 * Search result DTO, used to return the result of any search request
 * @param <T> can be used as wrapper
 */
public class SearchResultDto<T> {
    private List<T> content;
    private long total;
    private int page;
    private int size;
    private String searchAfter;
    private boolean hasMore;

    /**
     *
     * Search Result DTO
     *
     * @param content a List of results
     * @param total number of results in total
     * @param page used for pagination
     * @param size used for pagination
     * @param searchAfter string for pagination
     * @param hasMore boolean that shows if this is the last result page or not
     */
    public SearchResultDto( List<T> content, long total, int page, int size, String searchAfter, boolean hasMore) {
        this.content = content;
        this.total = total;
        this.page = page;
        this.size = size;
        this.searchAfter = searchAfter;
        this.hasMore = hasMore;
    }

    /**
     * default setter
     * @param hasMore boolean
     */
    public void setHasMore(boolean hasMore){
        this.hasMore = hasMore;
    }

    /**
     * default getter
     * @return boolean hasMore
     */
    public boolean getHasMore(){
        return hasMore;
    }

    /**
     * default getter
     * @return String searchAfter
     */
    public String getSearchAfter(){
        return searchAfter;
    }

    /**
     * default getter
     * @return List of Strings content
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * default getter
     * @return long total
     */
    public long getTotal() {
        return total;
    }

    /**
     * default getter
     * @return int page
     */
    public int getPage() {
        return page;
    }

    /**
     * default getter
     * @return int size
     */
    public int getSize() {
        return size;
    }
}
