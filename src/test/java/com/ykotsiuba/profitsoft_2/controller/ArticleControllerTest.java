package com.ykotsiuba.profitsoft_2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykotsiuba.profitsoft_2.Profitsoft2Application;
import com.ykotsiuba.profitsoft_2.TestProfitsoft2Application;
import com.ykotsiuba.profitsoft_2.dto.APIException;
import com.ykotsiuba.profitsoft_2.dto.article.*;
import com.ykotsiuba.profitsoft_2.entity.Article;
import com.ykotsiuba.profitsoft_2.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.ArticleMessages.*;
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

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_URL = "/articles";

    private static final String ID ="00000000-0000-0000-0000-000000000001";

    private static final String ID_URL = API_URL + "/%s";

    private static final String SEARCH_URL = API_URL + "/_list";

    private static final String REPORT_URL = API_URL + "/_report";

    private static final String UPLOAD_URL = API_URL + "/upload";

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
        ArticleDTO responseDTO = objectMapper.readValue(response, ArticleDTO.class);
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
        APIException responseDTO = objectMapper.readValue(response, APIException.class);
        assertEquals(String.format(ARTICLE_NOT_FOUND.getMessage(), invalidId), responseDTO.getErrors().get(0));
    }

    @Test
    @Transactional
    public void testSaveArticle() throws Exception {
        SaveArticleRequestDTO saveArticleRequestDTO = prepareSaveArticleRequest();
        String body = objectMapper.writeValueAsString(saveArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ArticleDTO responseDTO = objectMapper.readValue(response, ArticleDTO.class);
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
        String body = objectMapper.writeValueAsString(saveArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

          String response = mvcResult.getResponse().getContentAsString();
          APIException responseDTO = objectMapper.readValue(response, APIException.class);
          assertEquals(2, responseDTO.getErrors().size());
    }

    @Test
    @Transactional
    public void testUpdateArticle() throws Exception {
        SaveArticleRequestDTO updateArticleRequestDTO = prepareSaveArticleRequest();
        String body = objectMapper.writeValueAsString(updateArticleRequestDTO);

        MvcResult mvcResult = mvc.perform(put(String.format(ID_URL, ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ArticleDTO responseDTO = objectMapper.readValue(response, ArticleDTO.class);
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
        DeleteArticleResponseDTO responseDTO = objectMapper.readValue(response, DeleteArticleResponseDTO.class);
        assertEquals(ARTICLE_DELETED.getMessage(), responseDTO.getMessage());

        Optional<Article> byId = articleRepository.findById(UUID.fromString(ID));
        assertTrue(byId.isEmpty());
    }

    @Test
    public void testSearchArticles() throws Exception {
        SearchArticleRequestDTO requestDTO = prepareSearchRequest();
        String body = objectMapper.writeValueAsString(requestDTO);

        MvcResult mvcResult = mvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        SearchArticlesResponseDTO responseDTO = objectMapper.readValue(response, SearchArticlesResponseDTO.class);
        assertFalse(responseDTO.getList().isEmpty());
    }

    @Test
    public void testSearchArticles_invalidPaging() throws Exception {
        SearchArticleRequestDTO requestDTO = prepareSearchRequest();
        requestDTO.setPage(null);
        String body = objectMapper.writeValueAsString(requestDTO);

        MvcResult mvcResult = mvc.perform(post(SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        APIException responseDTO = objectMapper.readValue(response, APIException.class);
        assertEquals(1, responseDTO.getErrors().size());
    }

    @Test
    public void testGenerateReport() throws Exception {
        ReportArticlesRequestDTO requestDTO = prepareReportRequest();
        String body = objectMapper.writeValueAsString(requestDTO);

        mvc.perform(post(REPORT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=articles.xlsx"));
    }

    @Test
    @Transactional
    public void testUpload() throws Exception {
        final int initialCount = articleRepository.findAll().size();
        final int expectedUploaded = 3;
        final int expectedErrors = 7;
        MockMultipartFile file = prepareJsonFile();

        MvcResult mvcResult = mvc.perform(multipart(UPLOAD_URL)
                        .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        UploadArticlesResponseDTO responseDTO = objectMapper.readValue(response, UploadArticlesResponseDTO.class);
        assertEquals(expectedErrors, responseDTO.getErrors());
        assertEquals(expectedUploaded, responseDTO.getUploaded());

        List<Article> articles = articleRepository.findAll();
        assertEquals(initialCount + expectedUploaded, articles.size());
    }

    @Test
    public void testUpload_invalidFile() throws Exception {
        MockMultipartFile plainTextFile
                = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello world".getBytes()
        );

        MvcResult mvcResult = mvc.perform(multipart(UPLOAD_URL)
                        .file(plainTextFile)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        APIException responseDTO = objectMapper.readValue(response, APIException.class);
        assertEquals(FILE_NOT_VALID.getMessage(), responseDTO.getErrors().get(0));
    }
}