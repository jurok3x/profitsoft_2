package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.DeleteAuthorResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Author;
import com.ykotsiuba.profitsoft_2.mapper.AuthorMapper;
import com.ykotsiuba.profitsoft_2.repository.AuthorRepository;
import com.ykotsiuba.profitsoft_2.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private static final String AUTHOR_NOT_FOUND = "Author not found for ID: %s";

    private static final String AUTHOR_DELETED = "Deleted author with ID: %s";

    private final AuthorRepository authorRepository;

    private  final AuthorMapper authorMapper;

    @Override
    public AuthorDTO findById(String id) {
        Author author = findOrThrow(id);
        return authorMapper.toDTO(author);
    }

    private Author findOrThrow(String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Author> optionalAuthor = authorRepository.findById(uuid);
        return optionalAuthor.orElseThrow(() -> new EntityNotFoundException(String.format(AUTHOR_NOT_FOUND, id)));
    }

    @Override
    public AuthorDTO save(SaveAuthorRequestDTO requestDTO) {
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
        Author authorRequest = prepareAuthor(requestDTO);
        authorRequest.setArticles(author.getArticles());
        Author savedAuthor = authorRepository.save(authorRequest);
        return authorMapper.toDTO(savedAuthor);
    }

    @Override
    public DeleteAuthorResponseDTO delete(String id) {
        Author author = findOrThrow(id);
        authorRepository.delete(author);
        DeleteAuthorResponseDTO responseDTO = new DeleteAuthorResponseDTO();
        String message = String.format(AUTHOR_DELETED, author.getId());
        responseDTO.setMessage(message);
        return responseDTO;
    }
}
