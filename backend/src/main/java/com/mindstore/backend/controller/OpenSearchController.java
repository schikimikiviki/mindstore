package com.mindstore.backend.controller;

import com.mindstore.backend.data.TextIndex;
import com.mindstore.backend.service.TextIndexServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/text-index")
public class OpenSearchController {


    private final TextIndexServiceImpl textIndexServiceImpl;

    public OpenSearchController(TextIndexServiceImpl textIndexServiceImpl) {
        this.textIndexServiceImpl = textIndexServiceImpl;
    }

    @GetMapping("/all")
    public List<TextIndex> getAllTextIndexes(){
        return textIndexServiceImpl.findAll();
    }

}

