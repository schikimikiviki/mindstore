package com.mindstore.backend.service;

import com.mindstore.backend.data.entity.Text;
import com.mindstore.backend.repository.TextRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TextService {

    private final TextRepository textRepository;


    public TextService(TextRepository textRepository) {
        this.textRepository = textRepository;

    }

    public List<Text> findAll() {
        return textRepository.findAll();
    }


    public Optional<Text> findTextById(Integer id) {
        return textRepository.findById(id);
    }

    public void deleteTextById(Integer id) {
        textRepository.deleteById(id);

    }
}
