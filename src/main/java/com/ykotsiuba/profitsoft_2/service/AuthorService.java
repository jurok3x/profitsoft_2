package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.DeleteAuthorResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.SaveAuthorRequestDTO;

import java.util.List;

public interface AuthorService {
    AuthorDTO findById(String id);

    List<AuthorDTO> findAll();

    AuthorDTO save(SaveAuthorRequestDTO requestDTO);

    AuthorDTO update(SaveAuthorRequestDTO requestDTO, String id);

    DeleteAuthorResponseDTO delete(String id);
}
