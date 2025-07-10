package com.mindstore.backend.controller;

import com.mindstore.backend.data.Category;
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


    public SearchController(TextSearchService textSearchService, SearchHistoryService searchHistoryService) {

        //System.out.println("SearchController loaded");

        this.textSearchService = textSearchService;
        this.searchHistoryService = searchHistoryService;
    }

    @GetMapping
    public SearchResultDto<TextDocument> search(@RequestParam String query,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size, Principal principal) {

        // wenn wir eine Search machen, soll das auch als History abgespeichert werden
        searchHistoryService.saveSearch(query.toString());

        return textSearchService.search(query, page, size);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("It works");
    }


    @GetMapping("/history")
    public ResponseEntity<List<String>> getSearchHistory(Principal principal) {
      //  System.out.println("HISTORY ROUTE REQUESTED");
        return ResponseEntity.ok(searchHistoryService.getRecentSearches());
    }

    @GetMapping("/tags")
    public Category[] getTagList() {
        return textSearchService.findTags();
    }

}

