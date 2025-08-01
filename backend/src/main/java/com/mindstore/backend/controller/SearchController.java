package com.mindstore.backend.controller;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchHitDto;
import com.mindstore.backend.data.dto.SearchResultDto;
import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.repository.UserRepository;
import com.mindstore.backend.service.SearchHistoryService;
import com.mindstore.backend.service.TextSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
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

    /**
     * Searches through all available {@code TextDocument}s based on the query parameter.
     * Results are paginated and returned in a {@code SearchResultDto}.

     * Additionally, the query is saved to the search history.
     *
     * @param query      the search term entered by the user
     * @param page       the zero-based page index for pagination (default is 0)
     * @param size       the number of items per page (default is 10)
     * @param principal  the currently authenticated user (used for audit or filtering if needed)
     * @return a {@code SearchResultDto} containing the paginated list of matching {@code TextDocument}s
     */

    @GetMapping
    public SearchResultDto<TextDocument> search(@RequestParam String query,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "") String searchAfter,
                                                 Principal principal) {

        // wenn wir eine Search machen, soll das auch als History abgespeichert werden
        searchHistoryService.saveSearch(query.toString());

        return textSearchService.search(query, page, size, searchAfter);
    }

    @GetMapping("/tag-search")
    public SearchResultDto<TextDocument> searchThroughTags(@RequestParam String query,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "") String searchAfter, @RequestParam List<String> tags,
                                                Principal principal) {

        // diese Suche auch ins history service speichern
        searchHistoryService.saveSearch(query.toString());

        return textSearchService.searchThroughTaggedTexts(tags, query, page, size, searchAfter);
    }

    /**
     *
     * @param prefix - string we are searching for, for example "do" will suggest "docker"
     * @return List of strings that match query
     * @throws IOException when search fails
     */
    @GetMapping("/autocomplete")
    public List<String> autocompleteString(@RequestParam String prefix) throws IOException {
        return textSearchService.autocomplete(prefix);
    }


    /**
     * function: used to test the SearchController and debugging
     * @return ok ---> if successfull
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("It works");
    }


    /**
     *
     * @param principal the currently authenticated user
     * function: get the recent searches that were made by the user
     * @return a List of strings with the searches
     */
    @GetMapping("/history")
    public ResponseEntity<List<String>> getSearchHistory(Principal principal) {
      //  System.out.println("HISTORY ROUTE REQUESTED")
        return ResponseEntity.ok(searchHistoryService.getRecentSearches());
    }

    /**
     * function: used to get all the available values the CATEGORY enum can have
     * @return Array of Category enum
     */
    @GetMapping("/tags")
    public Category[] getTagList() {
        return textSearchService.findTags();
    }


    /**
     * Searches through all available {@code TextDocument}s based on the query parameter.
     * Results are paginated and returned in a {@code SearchResultDto}.

     * Additionally, the query is saved to the search history.
     *
     * @param query      the search term entered by the user
     * @param page       the zero-based page index for pagination (default is 0)
     * @param size       the number of items per page (default is 10)
     * @param principal  the currently authenticated user (used for audit or filtering if needed)
     * @param dateFrom   the date we are querying from
     * @param dateTo     the date we are querying to
     *
     * @return a {@code SearchResultDto} containing the paginated list of matching {@code TextDocument}s
     */

    @GetMapping("/time")
    public SearchResultDto<TextDocument> searchThroughTime(@RequestParam String query,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "") String searchAfter,
                                                           @RequestParam(required = false)String dateFrom,
                                                           @RequestParam(required = false)String dateTo,
                                                           Principal principal) {

        // wenn wir eine Search machen, soll das auch als History abgespeichert werden
        searchHistoryService.saveSearch(query.toString());

        return textSearchService.searchWithTimeSpan(query, page, size, searchAfter, dateFrom, dateTo);
    }


    /**
     * Searches through all available {@code TextDocument}s based on the query parameter.
     * Results are paginated and returned in a {@code SearchResultDto}.

     * Additionally, the query is saved to the search history.
     *
     * @param command    the command entered by the user
     * @param page       the zero-based page index for pagination (default is 0)
     * @param size       the number of items per page (default is 10)
     * @param principal  the currently authenticated user (used for audit or filtering if needed)
     *
     * @return a {@code SearchResultDto} containing the paginated list of matching {@code TextDocument}s
     */

    @GetMapping("/command-search")
    public SearchResultDto<TextDocument> searchCommandList(@RequestParam String command,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "") String searchAfter,
                                                           Principal principal) {

        // wenn wir eine Search machen, soll das auch als History abgespeichert werden
        searchHistoryService.saveSearch(command.toString());

        return textSearchService.searchForCommand(command, page, size, searchAfter);
    }


    /**
     * Searches through all available {@code TextDocument}s based on the query parameter.
     * Results are paginated and returned in a {@code SearchResultDto}.

     * Additionally, the query is saved to the search history.
     *
     * @param query      the search term entered by the user
     * @param page       the zero-based page index for pagination (default is 0)
     * @param size       the number of items per page (default is 10)
     * @param principal  the currently authenticated user (used for audit or filtering if needed)
     *
     * @return a {@code SearchResultDto} containing the paginated list of matching {@code TextDocument}s and a section of highlighted results
     */
    @GetMapping("/highlight")
    public SearchResultDto<SearchHitDto<TextDocument>> searchHighlighted(@RequestParam String query,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(defaultValue = "") String searchAfter,
                                                                         Principal principal) {

        // wenn wir eine Search machen, soll das auch als History abgespeichert werden
        searchHistoryService.saveSearch(query.toString());

        return textSearchService.searchHighlighted(query, page, size, searchAfter);
    }



}

