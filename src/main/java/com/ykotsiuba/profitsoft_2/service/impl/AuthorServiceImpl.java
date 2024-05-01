package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.author.DeleteAuthorResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.author.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.mapper.AuthorMapper;
import com.ykotsiuba.profitsoft_2.repository.AuthorRepository;
import com.ykotsiuba.profitsoft_2.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.AuthorMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private  final AuthorMapper authorMapper;

    @Override
    public List<AuthorDTO> findAll() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(authorMapper::toDTO)
                .toList();
    }

    private Author findOrThrow(String id) {
        log.info("Searching author with id: {}", id);
        UUID uuid = UUID.fromString(id);
        Optional<Author> optionalAuthor = authorRepository.findById(uuid);
        if(optionalAuthor.isEmpty()){
            log.error("Author with id {} not found", id);
        }
        return optionalAuthor.orElseThrow(() -> new EntityNotFoundException(String.format(AUTHOR_NOT_FOUND.getMessage(), id)));
    }

    @Override
    public AuthorDTO save(SaveAuthorRequestDTO requestDTO) {
        Optional<Author> byEmail = findByEmail(requestDTO.getEmail());
        if(byEmail.isPresent()) {
            log.error("Error saving author with email: {}", requestDTO.getEmail());
            throw new IllegalArgumentException(String.format(AUTHOR_ALREADY_EXISTS.getMessage(), requestDTO.getEmail()));
        }
        Author authorRequest = authorMapper.convertSaveRequestToEntity(requestDTO);
        Author savedAuthor = authorRepository.save(authorRequest);
        log.info("Saving author: {}", savedAuthor);
        return authorMapper.toDTO(savedAuthor);
    }

    private Optional<Author> findByEmail(String email) {
        log.info("Searching author with email: {}", email);
        return authorRepository.findByEmail(email);
    }

    @Override
    public AuthorDTO update(SaveAuthorRequestDTO requestDTO, String id) {
        Author author = findOrThrow(id);
        Optional<Author> byEmail = findByEmail(requestDTO.getEmail());
        if(byEmail.isPresent()) {
            if(!author.getEmail().equals(byEmail.get().getEmail())) {
                log.error("Error updating author with email: {}", requestDTO.getEmail());
                throw new IllegalArgumentException(AUTHOR_UPDATE_ERROR.getMessage());
            }
        }
        Author authorRequest = authorMapper.convertSaveRequestToEntity(requestDTO);
        authorRequest.setId(author.getId());
        authorRequest.setArticles(author.getArticles());
        Author updatedAuthor = authorRepository.save(authorRequest);
        log.info("Updating author: {}", updatedAuthor);
        return authorMapper.toDTO(updatedAuthor);
    }

    @Override
    public DeleteAuthorResponseDTO delete(String id) {
        Author author = findOrThrow(id);
        authorRepository.delete(author);
        DeleteAuthorResponseDTO responseDTO = new DeleteAuthorResponseDTO();
        responseDTO.setMessage(AUTHOR_DELETED.getMessage());
        log.info("Deleting author with id {}", id);
        return responseDTO;
    }
}
