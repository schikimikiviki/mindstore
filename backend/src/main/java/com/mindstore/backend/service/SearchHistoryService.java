package com.mindstore.backend.service;

import com.mindstore.backend.data.entity.SearchHistory;
import com.mindstore.backend.repository.SearchHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service class for search history entity
 */
@Service
public class SearchHistoryService {

    /**
     * Repository for the search history
     */
    public final SearchHistoryRepository searchHistoryRepository;

    /**
     * Search history service
     * @param searchHistoryRepository repository for search history
     */
    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository){
    this.searchHistoryRepository = searchHistoryRepository;
}

    /**
     * function: saving a search that the user made to the serach history repo
     * @param term the term that we want to save
     */
    public void saveSearch(String term) {
        SearchHistory history = new SearchHistory();
        history.setTerm(term);
        history.setSearchedAt(new Date());
        searchHistoryRepository.save(history);
    }

    /**
     *
     * function: We return a list of maximum 10 strings that represent the most recent searches
     * @return a list of strings with the recent searches
     */
    public List<String> getRecentSearches() {
        return searchHistoryRepository.findTop10ByOrderBySearchedAtDesc()
                .stream()
                .map(SearchHistory::getTerm)
                .distinct()
                .toList();
    }


}
