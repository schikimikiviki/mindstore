package com.mindstore.backend.controller;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextIndex;
import com.mindstore.backend.data.entity.User;
import com.mindstore.backend.service.TextIndexServiceImpl;
import com.mindstore.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OpenSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TextIndexServiceImpl textIndexServiceImpl;

    @Test
    void shouldReturnTextList() throws Exception {

        TextIndex textIndexOne = new TextIndex();
        textIndexOne.setTitle("Some random title");
        textIndexOne.setContent_html("<h1>Soem content</h1>");
        textIndexOne.setContent_raw("Some content");
        textIndexOne.setCommandList(List.of("sudo rm -rf", "sudo mkdir"));
        textIndexOne.setTags(List.of(Category.DATABASES, Category.AI));

        TextIndex textIndexTwo = new TextIndex();
        textIndexTwo.setTitle("Some random title 2");
        textIndexTwo.setContent_html("<h1>Soem content2</h1>");
        textIndexTwo.setContent_raw("Some content2");
        textIndexTwo.setCommandList(List.of("sudo rm -rf", "sudo mkdir"));
        textIndexTwo.setTags(List.of(Category.DOCKER, Category.LINUX));

        List<TextIndex> mockTexts = List.of(textIndexOne, textIndexTwo);

        when(textIndexServiceImpl.findAll()).thenReturn(mockTexts);

        String response = mockMvc.perform(get("/text-index/all"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println("Actual response: " + response);


        mockMvc.perform(get("/text-index/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Some random title"))
                .andExpect(jsonPath("$[1].title").value("Some random title 2"));
    }
}
