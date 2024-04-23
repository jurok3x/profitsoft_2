package com.ykotsiuba.profitsoft_2.controller;

import com.ykotsiuba.profitsoft_2.dto.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.DeleteAuthorResponseDTO;
import com.ykotsiuba.profitsoft_2.dto.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    public ResponseEntity<AuthorDTO> save(@RequestBody @Valid SaveAuthorRequestDTO requestDTO) {
        AuthorDTO responseDTO = service.save(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findAll() {
        List<AuthorDTO> authors = service.findAll();
        return ResponseEntity.ok(authors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable String id,
                                             @RequestBody @Valid SaveAuthorRequestDTO requestDTO) {
        AuthorDTO responseDTO = service.update(requestDTO, id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteAuthorResponseDTO> delete(@PathVariable String id) {
        DeleteAuthorResponseDTO responseDTO = service.delete(id);
        return ResponseEntity.ok(responseDTO);
    }
}
