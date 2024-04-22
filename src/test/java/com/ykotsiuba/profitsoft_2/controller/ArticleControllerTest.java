package com.ykotsiuba.profitsoft_2.controller;

import com.jayway.jsonpath.JsonPath;
import com.ykotsiuba.profitsoft_2.Profitsoft2Application;
import com.ykotsiuba.profitsoft_2.TestProfitsoft2Application;
import com.ykotsiuba.profitsoft_2.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Profitsoft2Application.class)
@Import(TestProfitsoft2Application.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testGetArticle() throws Exception {
        String id = "00000000-0000-0000-0000-000000000001";
        String url = String.format("/articles/%s", id);
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String journal = JsonPath.parse(response).read("$.journal");
        assertEquals("Physics Today", journal);
    }

    @Test
    @Transactional
    public void testSaveArticle() throws Exception {
        String url = "/articles";
        String body = """
       {
           "title": "%s",
           "field": "%s",
           "year": 2001,
           "journal": "%s",
           "authorId": "00000000-0000-0000-0000-000000000001"
       }              
     """.formatted("Lasers in our world", "PHYSICS", "Applied optics");
        MvcResult mvcResult = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String journal = JsonPath.parse(response).read("$.journal");
        assertEquals("Applied optics", journal);
    }

}