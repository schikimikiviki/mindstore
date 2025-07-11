package com.mindstore.backend.controller;

import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import com.mindstore.backend.service.JwtService;
import com.mindstore.backend.service.TextIndexService;
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

    public OpenSearchController(TextIndexService textIndexService, JwtService jwtService) {
        this.textIndexService = textIndexService;
        this.jwtService = jwtService;

    }

    // return the results in a DTO format
    @GetMapping("/all")
    public SearchResultDto<TextDocument> getAllTextIndexes(){
        List<TextDocument> docs = textIndexService.findAll();
        return new SearchResultDto<>(docs, docs.size(), 0, docs.size());
    }


    @GetMapping("/all/tags")
    public SearchResultDto<TextDocument> getAllTextIndexesWithTag(
            @RequestParam List<String> tags) {

        List<TextDocument> docs = textIndexService.findAllWithTags(tags);
        return new SearchResultDto<>(docs, docs.size(), 0, docs.size());
    }



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

