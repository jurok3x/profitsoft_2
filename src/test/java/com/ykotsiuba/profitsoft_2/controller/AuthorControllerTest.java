package com.ykotsiuba.profitsoft_2.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ykotsiuba.profitsoft_2.Profitsoft2Application;
import com.ykotsiuba.profitsoft_2.TestProfitsoft2Application;
import com.ykotsiuba.profitsoft_2.dto.APIException;
import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.author.DeleteAuthorResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.author.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.repository.AuthorRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.AuthorMessages.*;
import static com.ykotsiuba.profitsoft_2.utils.EntitySource.prepareSaveAuthorRequest;
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
class AuthorControllerTest {

    private static final ObjectMapper DEFAULT_MAPPER;

    private static final int INITIAL_AUTHOR_COUNT = 10;

    private static final String API_URL = "/authors";

    private static final String ID ="00000000-0000-0000-0000-000000000001";

    private static final String ID_URL = API_URL + "/%s";

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
    private AuthorRepository authorRepository;

    @Test
    public void testGetAuthors() throws Exception {
        MvcResult mvcResult = mvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<AuthorDTO> responseDTO = DEFAULT_MAPPER.readValue(response, new TypeReference<List<AuthorDTO>>() {});
        assertEquals(INITIAL_AUTHOR_COUNT, responseDTO.size());

        List<Author> authors = authorRepository.findAll();
        assertEquals(INITIAL_AUTHOR_COUNT, authors.size());
    }

    @Test
    @Transactional
    public void testSaveAuthor() throws Exception {
        SaveAuthorRequestDTO saveAuthorRequestDTO = prepareSaveAuthorRequest();
        String body = DEFAULT_MAPPER.writeValueAsString(saveAuthorRequestDTO);

        MvcResult mvcResult = mvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        AuthorDTO responseDTO = DEFAULT_MAPPER.readValue(response, AuthorDTO.class);
        UUID id = responseDTO.getId();
        assertNotNull(id);

        Optional<Author> byId = authorRepository.findById(id);
        assertFalse(byId.isEmpty());
    }

    @Test
    @Transactional
    public void testSaveAuthor_invalidParameters() throws Exception {
        SaveAuthorRequestDTO saveAuthorRequestDTO = prepareSaveAuthorRequest();
        saveAuthorRequestDTO.setFirstName(null);
        saveAuthorRequestDTO.setLastName(null);
        String body = DEFAULT_MAPPER.writeValueAsString(saveAuthorRequestDTO);

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
    public void testSaveAuthor_emailExists() throws Exception {
        String existingEmail = "john.doe@example.com";
        SaveAuthorRequestDTO saveAuthorRequestDTO = prepareSaveAuthorRequest();
        saveAuthorRequestDTO.setEmail(existingEmail);
        String body = DEFAULT_MAPPER.writeValueAsString(saveAuthorRequestDTO);

        MvcResult mvcResult = mvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        APIException responseDTO = DEFAULT_MAPPER.readValue(response, APIException.class);
        String error = responseDTO.getErrors().get(0);
        assertEquals(String.format(AUTHOR_ALREADY_EXISTS.getMessage(), existingEmail), error);
    }

    @Test
    @Transactional
    public void testUpdateAuthor() throws Exception {
        SaveAuthorRequestDTO updateAuthorRequestDTO = prepareSaveAuthorRequest();
        String body = DEFAULT_MAPPER.writeValueAsString(updateAuthorRequestDTO);

        MvcResult mvcResult = mvc.perform(put(String.format(ID_URL, ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        AuthorDTO responseDTO = DEFAULT_MAPPER.readValue(response, AuthorDTO.class);
        assertEquals(updateAuthorRequestDTO.getFirstName(), responseDTO.getFirstName());

        Optional<Author> byId = authorRepository.findById(UUID.fromString(ID));
        assertFalse(byId.isEmpty());
        Author author = byId.get();
        assertEquals(updateAuthorRequestDTO.getFirstName(), author.getFirstName());
    }

    @Test
    @Transactional
    public void testUpdateAuthor_invalidEmail() throws Exception {
        String existingEmail = "jane.smith@example.com";
        SaveAuthorRequestDTO updateAuthorRequestDTO = prepareSaveAuthorRequest();
        updateAuthorRequestDTO.setEmail(existingEmail);
        String body = DEFAULT_MAPPER.writeValueAsString(updateAuthorRequestDTO);

        MvcResult mvcResult = mvc.perform(put(String.format(ID_URL, ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        APIException responseDTO = DEFAULT_MAPPER.readValue(response, APIException.class);
        String error = responseDTO.getErrors().get(0);
        assertEquals(String.format(AUTHOR_UPDATE_ERROR.getMessage(), existingEmail), error);
    }

    @Test
    @Transactional
    public void testDeleteAuthor() throws Exception {
        MvcResult mvcResult = mvc.perform(delete(String.format(ID_URL, ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        DeleteAuthorResponseDTO responseDTO = DEFAULT_MAPPER.readValue(response, DeleteAuthorResponseDTO.class);
        assertEquals(AUTHOR_DELETED.getMessage(), responseDTO.getMessage());

        Optional<Author> byId = authorRepository.findById(UUID.fromString(ID));
        assertTrue(byId.isEmpty());
    }

}