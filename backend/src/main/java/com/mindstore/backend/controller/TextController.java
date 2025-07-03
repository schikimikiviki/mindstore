package com.mindstore.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mindstore.backend.data.entity.Text;
import com.mindstore.backend.data.dto.TextDto;
import com.mindstore.backend.service.TextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public Optional<Text> getTextById(@PathVariable Integer Id) {
        return textService.findTextById(Id);
    }

//    @PostMapping
//    public Text addText(@RequestBody TextDto textDto) {
//        Text t1 = new Text();
//        t1.setTitle(textDto.getTitle());
//        t1.setContent_raw(textDto.getContent_raw());
//        t1.setContent_html(textDto.getContent_html());
//        t1.setTags(textDto.getTags());
//        t1.setCommandList(textDto.getCommandList());
//
//        return textService.save(t1);
//    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteText(@PathVariable Integer userId) {
        try {
            textService.deleteTextById(userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Text  deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Text not found"));
        }
    }



}
