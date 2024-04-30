package com.ykotsiuba.profitsoft_2.service;

import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.author.DeleteAuthorResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.author.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    /**
     * Retrieves all authors.
     *
     * @return A list of authors.
     */
    List<AuthorDTO> findAll();

    /**
     * Saves a new author.
     *
     * @param requestDTO The request object containing author details.
     * @return The saved author DTO.
     * @throws IllegalArgumentException If an author with the given email already exists.
     */
    AuthorDTO save(SaveAuthorRequestDTO requestDTO);

    /**
     * Updates an existing author.
     *
     * @param requestDTO The request object containing updated author details.
     * @param id The ID of the author to update.
     * @return The updated author DTO.
     * @throws IllegalArgumentException If email in update request already belongs to other user.
     * @throws jakarta.persistence.EntityNotFoundException if author not found.
     */
    AuthorDTO update(SaveAuthorRequestDTO requestDTO, String id);

    /**
     * Deletes an author by ID.
     *
     * @param id The ID of the author to delete.
     * @return The response DTO indicating the deletion status.
     * @throws jakarta.persistence.EntityNotFoundException if author not found.
     */
    DeleteAuthorResponseDTO delete(String id);
}
