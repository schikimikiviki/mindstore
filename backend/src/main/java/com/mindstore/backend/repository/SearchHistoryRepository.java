package com.mindstore.backend.repository;

import com.mindstore.backend.data.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

/**
 * Default Repository for Search History entity
 */
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    /**
     * function that returns the last 10 search Results
     * @return List of SearchHistory
     */
    List<SearchHistory> findTop10ByOrderBySearchedAtDesc();

}

