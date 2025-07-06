package com.mindstore.backend.controller;

import com.mindstore.backend.data.TextIndex;
import com.mindstore.backend.service.JwtService;
import com.mindstore.backend.service.TextIndexServiceImpl;
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


    private final TextIndexServiceImpl textIndexServiceImpl;
    private final JwtService jwtService;

    public OpenSearchController(TextIndexServiceImpl textIndexServiceImpl, JwtService jwtService) {
        this.textIndexServiceImpl = textIndexServiceImpl;
        this.jwtService = jwtService;

    }

    @GetMapping("/all")
    public List<TextIndex> getAllTextIndexes(){
        return textIndexServiceImpl.findAll();
    }


    @PostMapping("/create")
    public ResponseEntity<TextIndex> createTextIndex(@RequestBody TextIndex textIndex, HttpServletResponse response) {
        textIndexServiceImpl.indexText(textIndex);

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

