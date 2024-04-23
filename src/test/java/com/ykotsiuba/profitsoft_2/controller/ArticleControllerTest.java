package com.ykotsiuba.profitsoft_2.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ykotsiuba.profitsoft_2.Profitsoft2Application;
import com.ykotsiuba.profitsoft_2.TestProfitsoft2Application;
import com.ykotsiuba.profitsoft_2.dto.APIException;
import com.ykotsiuba.profitsoft_2.dto.ArticleDTO;
import com.ykotsiuba.profitsoft_2.dto.DeleteArticleResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.SaveArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Profitsoft2Application.class)
@Import(TestProfitsoft2Application.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ArticleControllerTest {

    private static final ObjectMapper DEFAULT_MAPPER;

    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        DEFAULT_MAPPER = mapper;
    }

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
        ArticleDTO responseDTO = DEFAULT_MAPPER.readValue(response, ArticleDTO.class);
        assertEquals("Physics Today", responseDTO.getJournal());
    }

    @Test
    public void testGetArticle_notFound() throws Exception {
        String id = "00000000-0000-0000-0000-000000000099";
        String url = String.format("/articles/%s", id);
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        APIException responseDTO = DEFAULT_MAPPER.readValue(response, APIException.class);
        assertEquals(String.format("Article not found for ID: %s", id), responseDTO.getErrors().get(0));
    }

    @Test
    @Transactional
    public void testSaveArticle() throws Exception {
        String url = "/articles";
        SaveArticleRequestDTO saveArticleRequestDTO = prepareSaveRequest();
        String body = DEFAULT_MAPPER.writeValueAsString(saveArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ArticleDTO responseDTO = DEFAULT_MAPPER.readValue(response, ArticleDTO.class);
        UUID id = responseDTO.getId();
        assertNotNull(id);

        Optional<Article> byId = articleRepository.findById(id);
        assertNotNull(byId.get());
    }

    @Test
    @Transactional
    public void testSaveArticle_Error() throws Exception {
        String url = "/articles";
        SaveArticleRequestDTO saveArticleRequestDTO = prepareSaveRequest();
        saveArticleRequestDTO.setTitle(null);
        saveArticleRequestDTO.setJournal(null);
        String body = DEFAULT_MAPPER.writeValueAsString(saveArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

          String response = mvcResult.getResponse().getContentAsString();
          APIException responseDTO = DEFAULT_MAPPER.readValue(response, APIException.class);
          assertEquals(2, responseDTO.getErrors().size());
    }

    @Test
    @Transactional
    public void testUpdateArticle() throws Exception {
        String id = "00000000-0000-0000-0000-000000000001";
        String url = String.format("/articles/%s", id);
        SaveArticleRequestDTO updateArticleRequestDTO = prepareSaveRequest();
        String body = DEFAULT_MAPPER.writeValueAsString(updateArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ArticleDTO responseDTO = DEFAULT_MAPPER.readValue(response, ArticleDTO.class);
        assertEquals(updateArticleRequestDTO.getTitle(), responseDTO.getTitle());

        Optional<Article> byId = articleRepository.findById(UUID.fromString(id));
        assertFalse(byId.isEmpty());
        Article article = byId.get();
        assertEquals(updateArticleRequestDTO.getTitle(), article.getTitle());
    }

    @Test
    @Transactional
    public void testDeleteArticle() throws Exception {
        String id = "00000000-0000-0000-0000-000000000001";
        String url = String.format("/articles/%s", id);
        MvcResult mvcResult = mvc.perform(delete(url))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        DeleteArticleResponseDTO responseDTO = DEFAULT_MAPPER.readValue(response, DeleteArticleResponseDTO.class);
        assertEquals("Article deleted successfully.", responseDTO.getMessage());

        Optional<Article> byId = articleRepository.findById(UUID.fromString(id));
        assertTrue(byId.isEmpty());
    }

    private SaveArticleRequestDTO prepareSaveRequest() {
        return SaveArticleRequestDTO.builder()
                .title("Lasers in our world")
                .authorId("00000000-0000-0000-0000-000000000001")
                .field("PHYSICS")
                .journal("Applied optics")
                .year(2001)
                .build();
    }

}