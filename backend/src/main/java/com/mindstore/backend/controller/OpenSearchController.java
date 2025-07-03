package com.mindstore.backend.controller;

import com.mindstore.backend.data.TextIndex;
import com.mindstore.backend.data.dto.SearchResultDto;
import com.mindstore.backend.data.entity.Text;
import com.mindstore.backend.service.TextIndexService;
import com.mindstore.backend.service.TextSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/text-index")
public class OpenSearchController {


    private final TextIndexService textIndexService;

    public OpenSearchController(TextIndexService textIndexService) {
        this.textIndexService = textIndexService;
    }

    @GetMapping("/all")
    public List<TextIndex> getAllTextIndexes(){
        return textIndexService.findAll();
    }

}

