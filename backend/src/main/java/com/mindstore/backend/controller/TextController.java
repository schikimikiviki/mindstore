package com.mindstore.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mindstore.backend.data.Text;
import com.mindstore.backend.service.TextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/texts")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;

    }

    @GetMapping("/all")
    public List<Text> getAllTexts(){
        return textService.findAll();
    }

    @GetMapping("/id/{Id}")
    public Optional<Text> getTextById(@PathVariable Long Id) {
        return textService.findTextById(Id);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteText(@PathVariable Long userId) {
        try {
            textService.deleteTextById(userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Text  deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Text not found"));
        }
    }



}
