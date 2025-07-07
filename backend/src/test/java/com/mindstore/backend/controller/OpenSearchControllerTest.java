package com.mindstore.backend.controller;

import com.mindstore.backend.data.Category;
import com.mindstore.backend.data.TextDocument;
import com.mindstore.backend.service.TextIndexService;
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
    private TextIndexService textIndexService;

    @Test
    void shouldReturnTextList() throws Exception {

        TextDocument textDocumentOne = new TextDocument();
        textDocumentOne.setTitle("Some random title");
        textDocumentOne.setContent_html("<h1>Soem content</h1>");
        textDocumentOne.setContent_raw("Some content");
        textDocumentOne.setCommandList(List.of("sudo rm -rf", "sudo mkdir"));
        textDocumentOne.setTags(List.of(Category.DATABASES, Category.AI));

        TextDocument textDocumentTwo = new TextDocument();
        textDocumentTwo.setTitle("Some random title 2");
        textDocumentTwo.setContent_html("<h1>Soem content2</h1>");
        textDocumentTwo.setContent_raw("Some content2");
        textDocumentTwo.setCommandList(List.of("sudo rm -rf", "sudo mkdir"));
        textDocumentTwo.setTags(List.of(Category.DOCKER, Category.LINUX));

        List<TextDocument> mockTexts = List.of(textDocumentOne, textDocumentTwo);

        when(textIndexService.findAll()).thenReturn(mockTexts);

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
