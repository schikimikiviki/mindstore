package com.mindstore.backend.repository;

import com.mindstore.backend.data.entity.SearchHistory;
import com.mindstore.backend.data.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findTop10ByOrderBySearchedAtDesc();

    List<SearchHistory> findTop10ByUserOrderBySearchedAtDesc(User user);

    @Query("SELECT sh.term, COUNT(sh.term) as cnt FROM SearchHistory sh GROUP BY sh.term ORDER BY cnt DESC")
    List<Object[]> findTopSearchTerms(Pageable pageable);
}

