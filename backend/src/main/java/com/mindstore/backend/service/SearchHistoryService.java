package com.mindstore.backend.service;

import com.mindstore.backend.data.entity.SearchHistory;
import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.repository.SearchHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SearchHistoryService {

public final SearchHistoryRepository searchHistoryRepository;

public SearchHistoryService(SearchHistoryRepository searchHistoryRepository){
    this.searchHistoryRepository = searchHistoryRepository;
}

    public void saveSearch(String term) {
        SearchHistory history = new SearchHistory();
        history.setTerm(term);
        history.setSearchedAt(new Date());
        searchHistoryRepository.save(history);
    }

    public List<String> getRecentSearches() {
        return searchHistoryRepository.findTop10ByOrderBySearchedAtDesc()
                .stream()
                .map(SearchHistory::getTerm)
                .distinct()
                .toList();
    }


}
