package com.mindstore.backend.security;
import com.mindstore.backend.repository.UserRepository;
import com.mindstore.backend.service.TextIndexServiceImpl;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitializerTest {

    @Mock
    private TextIndexServiceImpl textIndexServiceImpl;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private Initializer initializer;

    @Test
    void checkUserCreation() throws Exception {
        initializer.run();
        verify(userRepository).findByEmail("test@gmx.at");
    }

    @Test
    void checkOpensearchEntries() throws Exception {

        // we want there to be as many entries as in the JSON data file
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(new FileReader("src/data/data.json"));
        Long expectedDataSize = (long) array.size();

        when(textIndexServiceImpl.countTexts()).thenReturn((long) expectedDataSize);
        long actualSize = textIndexServiceImpl.countTexts();

        assertEquals(expectedDataSize, actualSize);

    }
}
