package com.mindstore.backend.service;

import com.mindstore.backend.data.Text;
import com.mindstore.backend.repository.TextRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TextService {

    private final TextRepository textRepository;
    private final TextIndexService textIndexService;

    public TextService(TextRepository textRepository, TextIndexService textIndexService) {
        this.textRepository = textRepository;
        this.textIndexService = textIndexService;
    }

    public List<Text> findAll() {
        return textRepository.findAll();
    }

    public Text save(Text text) {
        Text savedText = textRepository.save(text);
        textIndexService.indexText(savedText);
        return savedText;
    }

    public Optional<Text> findTextById(Integer id) {
        return textRepository.findById(id);
    }

    public void deleteTextById(Integer id) {
        textRepository.deleteById(id);
        textIndexService.deleteTextIndex(id);
    }
}
