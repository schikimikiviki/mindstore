package com.mindstore.backend.controller;

import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
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
    public SearchResultDto<TextDocument> search(@RequestParam String query,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return textSearchService.search(query, page, size);
    }
}
