package com.mindstore.backend.security;
import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.data.dto.SearchResultDto;
import com.mindstore.backend.repository.UserRepository;
import com.mindstore.backend.service.TextIndexService;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitializerTest {

    @Mock
    private TextIndexService textIndexService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private Initializer initializer;

    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     *
     * function: check if the test user was created successfully when initializing the app
     * @throws Exception if the initialiter throws an error
     */
    @Test
    void checkUserCreation() throws Exception {
        initializer.run();
        verify(userRepository).findByEmail("test@test.at");
    }


}
