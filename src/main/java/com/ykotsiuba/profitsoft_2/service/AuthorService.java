package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.DeleteAuthorResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorDTO findById(String id);

    Optional<Author> findByEmail(String email);

    List<AuthorDTO> findAll();

    AuthorDTO save(SaveAuthorRequestDTO requestDTO);

    AuthorDTO update(SaveAuthorRequestDTO requestDTO, String id);

    DeleteAuthorResponseDTO delete(String id);
}
