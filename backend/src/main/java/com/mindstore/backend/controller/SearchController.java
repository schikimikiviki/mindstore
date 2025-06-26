package com.mindstore.backend.controller;

import com.mindstore.backend.data.TextIndex;
import com.mindstore.backend.data.SearchResult;
import com.mindstore.backend.service.TextSearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final TextSearchService textSearchService;

    public SearchController(TextSearchService textSearchService) {
        this.textSearchService = textSearchService;
    }

    @GetMapping
    public SearchResult<TextIndex> search(@RequestParam String query,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return textSearchService.search(query, page, size);
    }
}
