package com.ykotsiuba.profitsoft_2.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ykotsiuba.profitsoft_2.Profitsoft2Application;
import com.ykotsiuba.profitsoft_2.TestProfitsoft2Application;
import com.ykotsiuba.profitsoft_2.dto.APIException;
import com.ykotsiuba.profitsoft_2.dto.article.*;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.ArticleMessages.ARTICLE_DELETED;
import static com.ykotsiuba.profitsoft_2.entity.enums.ArticleMessages.ARTICLE_NOT_FOUND;
import static com.ykotsiuba.profitsoft_2.utils.EntitySource.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Profitsoft2Application.class)
@Import(TestProfitsoft2Application.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ArticleControllerTest {

    private static final ObjectMapper DEFAULT_MAPPER;

    private static final String API_URL = "/articles";

    private static final String ID ="00000000-0000-0000-0000-000000000001";

    private static final String ID_URL = API_URL + "/%s";

    private static final String SEARCH_URL = API_URL + "/_list";

    private static final String REPORT_URL = API_URL + "/_report";

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
        MvcResult mvcResult = mvc.perform(get(String.format(ID_URL, ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ArticleDTO responseDTO = DEFAULT_MAPPER.readValue(response, ArticleDTO.class);
        assertEquals("Physics Today", responseDTO.getJournal());
    }

    @Test
    public void testGetArticle_notFound() throws Exception {
        String invalidId = "00000000-0000-0000-0000-000000000099";
        MvcResult mvcResult = mvc.perform(get(String.format(ID_URL, invalidId)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        APIException responseDTO = DEFAULT_MAPPER.readValue(response, APIException.class);
        assertEquals(String.format(ARTICLE_NOT_FOUND.getMessage(), invalidId), responseDTO.getErrors().get(0));
    }

    @Test
    @Transactional
    public void testSaveArticle() throws Exception {
        SaveArticleRequestDTO saveArticleRequestDTO = prepareSaveArticleRequest();
        String body = DEFAULT_MAPPER.writeValueAsString(saveArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(post(API_URL)
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
        assertFalse(byId.isEmpty());
    }

    @Test
    @Transactional
    public void testSaveArticle_invalidParameters() throws Exception {
        SaveArticleRequestDTO saveArticleRequestDTO = prepareSaveArticleRequest();
        saveArticleRequestDTO.setTitle(null);
        saveArticleRequestDTO.setJournal(null);
        String body = DEFAULT_MAPPER.writeValueAsString(saveArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(post(API_URL)
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
        SaveArticleRequestDTO updateArticleRequestDTO = prepareSaveArticleRequest();
        String body = DEFAULT_MAPPER.writeValueAsString(updateArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(put(String.format(ID_URL, ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ArticleDTO responseDTO = DEFAULT_MAPPER.readValue(response, ArticleDTO.class);
        assertEquals(updateArticleRequestDTO.getTitle(), responseDTO.getTitle());

        Optional<Article> byId = articleRepository.findById(UUID.fromString(ID));
        assertFalse(byId.isEmpty());
        Article article = byId.get();
        assertEquals(updateArticleRequestDTO.getTitle(), article.getTitle());
    }

    @Test
    @Transactional
    public void testDeleteArticle() throws Exception {
        MvcResult mvcResult = mvc.perform(delete(String.format(ID_URL, ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        DeleteArticleResponseDTO responseDTO = DEFAULT_MAPPER.readValue(response, DeleteArticleResponseDTO.class);
        assertEquals(ARTICLE_DELETED.getMessage(), responseDTO.getMessage());

        Optional<Article> byId = articleRepository.findById(UUID.fromString(ID));
        assertTrue(byId.isEmpty());
    }

    @Test
    public void testSearchArticles() throws Exception {
        SearchArticleRequestDTO requestDTO = prepareSearchRequest();
        String body = DEFAULT_MAPPER.writeValueAsString(requestDTO);

        MvcResult mvcResult = mvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        SearchArticlesResponseDTO responseDTO = DEFAULT_MAPPER.readValue(response, SearchArticlesResponseDTO.class);
        assertFalse(responseDTO.getList().isEmpty());
    }

    @Test
    public void testSearchArticles_invalidPaging() throws Exception {
        SearchArticleRequestDTO requestDTO = prepareSearchRequest();
        requestDTO.setPage(null);
        String body = DEFAULT_MAPPER.writeValueAsString(requestDTO);

        MvcResult mvcResult = mvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        APIException responseDTO = DEFAULT_MAPPER.readValue(response, APIException.class);
        assertEquals(1, responseDTO.getErrors().size());
    }

    @Test
    public void testGenerateReport() throws Exception {
        ReportArticlesRequestDTO requestDTO = prepareReportRequest();
        String body = DEFAULT_MAPPER.writeValueAsString(requestDTO);

        mvc.perform(post(REPORT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=articles.xlsx"));
    }
}