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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ykotsiuba.profitsoft_2.entity.enums.AuthorMessages.*;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private  final AuthorMapper authorMapper;

    @Override
    public AuthorDTO findById(String id) {
        Author author = findOrThrow(id);
        return authorMapper.toDTO(author);
    }

    @Override
    public Optional<Author> findByEmail(String email) {
        return authorRepository.findByEmail(email);
    }

    @Override
    public List<AuthorDTO> findAll() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(authorMapper::toDTO)
                .toList();
    }

    private Author findOrThrow(String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Author> optionalAuthor = authorRepository.findById(uuid);
        return optionalAuthor.orElseThrow(() -> new EntityNotFoundException(String.format(AUTHOR_NOT_FOUND.getMessage(), id)));
    }

    @Override
    public AuthorDTO save(SaveAuthorRequestDTO requestDTO) {
        Optional<Author> byEmail = findByEmail(requestDTO.getEmail());
        if(byEmail.isPresent()) {
            throw new IllegalArgumentException(String.format(AUTHOR_ALREADY_EXISTS.getMessage(), requestDTO.getEmail()));
        }
        Author authorRequest = prepareAuthor(requestDTO);
        Author savedAuthor = authorRepository.save(authorRequest);
        return authorMapper.toDTO(savedAuthor);
    }

    private Author prepareAuthor(SaveAuthorRequestDTO requestDTO) {
        return Author.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .build();
    }

    @Override
    public AuthorDTO update(SaveAuthorRequestDTO requestDTO, String id) {
        Author author = findOrThrow(id);
        Optional<Author> byEmail = findByEmail(requestDTO.getEmail());
        if(byEmail.isPresent()) {
            if(!author.getEmail().equals(byEmail.get().getEmail())) {
                throw new IllegalArgumentException(AUTHOR_UPDATE_ERROR.getMessage());
            }
        }
        Author authorRequest = prepareAuthor(requestDTO);
        authorRequest.setId(author.getId());
        authorRequest.setArticles(author.getArticles());
        Author updatedAuthor = authorRepository.save(authorRequest);
        return authorMapper.toDTO(updatedAuthor);
    }

    @Override
    public DeleteAuthorResponseDTO delete(String id) {
        Author author = findOrThrow(id);
        authorRepository.delete(author);
        DeleteAuthorResponseDTO responseDTO = new DeleteAuthorResponseDTO();
        responseDTO.setMessage(AUTHOR_DELETED.getMessage());
        return responseDTO;
    }
}
