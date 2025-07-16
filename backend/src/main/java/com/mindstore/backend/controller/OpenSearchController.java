package com.mindstore.backend.controller;

import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import com.mindstore.backend.service.JwtService;
import com.mindstore.backend.service.TextIndexService;
import com.mindstore.backend.service.TextSearchService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/text-index")
public class OpenSearchController {


    private final TextIndexService textIndexService;
    private final JwtService jwtService;
    private final TextSearchService textSearchService;

    public OpenSearchController(TextIndexService textIndexService, JwtService jwtService, TextSearchService textSearchService) {
        this.textIndexService = textIndexService;
        this.jwtService = jwtService;
        this.textSearchService = textSearchService;
    }

    /**
     *
     * @param searchAfter - searchAfter string that references the createdAt
     * @param size - the size of the result to be fetched
     * function: used to return all Text documents available
     * @return a SearchResultDto with the TextDocuments
     */
    @GetMapping("/all")
    public SearchResultDto<TextDocument> getAllTextIndexes(
            @RequestParam(defaultValue = "") String searchAfter,
            @RequestParam(defaultValue = "100") int size
    ) {
        return textSearchService.findAll(searchAfter, size);
    }


    /**
     *
     * @param searchAfter - searchAfter string that references the createdAt
     * @param size - the size of the result to be fetched
     * @param tags that are searched
     * function: used to get all text documents that contain a specific tag
     *  or a list of tags, used in the frontend to filter for specific tags
     * @return a SearchResultDto with the text documents
     */
    @GetMapping("/all/tags")
    public SearchResultDto<TextDocument> getAllTextIndexesWithTag(
            @RequestParam List<String> tags, @RequestParam(defaultValue = "") String searchAfter,
            @RequestParam(defaultValue = "100") int size) {

        return textSearchService.findAllWithTags(tags, searchAfter, size);
        //return new SearchResultDto<>(docs, docs.size(), 0, docs.size());
    }

    /**
     *
     * @param textDocument with title, content, taglist, etc.
     * @param response - the extended response
     * function: used to create new text documents, used in the frontend so that the user can add new entries
     * @return 201 CREATED --> if successfull
     */
    @PostMapping("/create")
    public ResponseEntity<TextDocument> createTextIndex(@RequestBody TextDocument textDocument, HttpServletResponse response) {
        textIndexService.indexText(textDocument);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        long jwtExpiration = jwtService.getExpirationTime();
        long longerExpiration = jwtExpiration + 3600000; // add 1 hour
        String token = jwtService.generateToken(new HashMap<>(), userDetails, longerExpiration);

        // use the new token as cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int)(longerExpiration / 1000));
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}

