package com.mindstore.backend.controller;

import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.repository.UserRepository;
import com.mindstore.backend.service.SearchHistoryService;
import com.mindstore.backend.service.TextSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final TextSearchService textSearchService;
    private final SearchHistoryService searchHistoryService;
    private final UserRepository userRepository;

    public SearchController(TextSearchService textSearchService, SearchHistoryService searchHistoryService, UserRepository userRepository) {

        System.out.println("SearchController loaded");
        
        this.textSearchService = textSearchService;
        this.searchHistoryService = searchHistoryService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public SearchResultDto<TextDocument> search(@RequestParam String query,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size, Principal principal) {

        // wenn wir eine Search machen, soll das auch als History abgespeichert werden
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        searchHistoryService.saveSearch(query.toString(), user);

        return textSearchService.search(query, page, size);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("It works");
    }


    @GetMapping("/history")
    public ResponseEntity<List<String>> getSearchHistory(Principal principal) {
        System.out.println("HISTORY ROUTE REQUESTED");
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        return ResponseEntity.ok(searchHistoryService.getRecentSearches(user));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<String>> getPopularSearches() {
        return ResponseEntity.ok(searchHistoryService.getPopularSearches());
    }
}

