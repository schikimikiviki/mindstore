package com.mindstore.backend.data.entity;

import jakarta.persistence.*;

import java.util.Date;

/**
 * Class that is used to save past searches
 */
@Table(name = "search_history")
@Entity
public class SearchHistory {

    /**
     * default constructor
     */
    public SearchHistory(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String term;

    private Date searchedAt = new Date();

    /**
     * default getter
     * @return id Long
     */
    public Long getId() {
        return id;
    }

    /**
     * default setter
     * @param id long
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * default getter
     * @return term string
     */
    public String getTerm() {
        return term;
    }

    /**
     * default setter
     * @param term String
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * default getter
     * @return searchedAt Date
     */
    public Date getSearchedAt() {
        return searchedAt;
    }

    /**
     * default setter
     * @param searchedAt Date
     */
    public void setSearchedAt(Date searchedAt) {
        this.searchedAt = searchedAt;
    }

}

