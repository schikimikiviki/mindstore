package com.mindstore.backend.repository;

import com.mindstore.backend.data.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<Text, Integer> {


    boolean existsByTitle(String title);
}
