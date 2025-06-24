package com.mindstore.backend.service;

import java.util.List;
import java.util.Optional;

import com.mindstore.backend.data.Text;
import com.mindstore.backend.repository.TextRepository;
import org.springframework.stereotype.Service;

@Service
public class TextService {

    private final TextRepository textRepository;


    public TextService(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    public List<Text> findAll() {
        return textRepository.findAll();
    }


    public Text save(Text user) {
        return textRepository.save(user);
    }

    public Optional<Text> findTextById (Long id) {
        return textRepository.findById(id);
    }

    public void deleteTextById(Long id) {
        textRepository.deleteById(id);
    }






}
