package com.mindstore.backend.repository;

import com.mindstore.backend.data.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findTop10ByOrderBySearchedAtDesc();

}

