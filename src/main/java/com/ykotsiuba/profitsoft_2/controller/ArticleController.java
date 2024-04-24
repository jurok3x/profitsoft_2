package com.ykotsiuba.profitsoft_2.controller;

import com.ykotsiuba.profitsoft_2.dto.*;
import com.ykotsiuba.profitsoft_2.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService service;

    @PostMapping
    public ResponseEntity<ArticleDTO> save(@RequestBody @Valid SaveArticleRequestDTO requestDTO) {
        ArticleDTO responseDTO = service.save(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> findById(@PathVariable String id) {
        ArticleDTO responseDTO = service.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> update(@PathVariable String id,
                                             @RequestBody @Valid SaveArticleRequestDTO requestDTO) {
        ArticleDTO responseDTO = service.update(requestDTO, id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteArticleResponseDTO> delete(@PathVariable String id) {
        DeleteArticleResponseDTO responseDTO = service.delete(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/_list")
    public ResponseEntity<SearchArticlesResponseDTO> search(@RequestBody @Valid SearchArticleRequestDTO requestDTO) {
        SearchArticlesResponseDTO responseDTO = service.findBySearchParameters(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
