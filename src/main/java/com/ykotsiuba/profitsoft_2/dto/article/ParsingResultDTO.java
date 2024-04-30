package com.ykotsiuba.profitsoft_2.dto.article;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParsingResultDTO {
    List<UploadArticleRequestDTO> validRequests;

    List<UploadArticleRequestDTO> invalidRequests;

    public ParsingResultDTO() {
        validRequests =new ArrayList<>();
        invalidRequests =new ArrayList<>();
    }

    public void addValidRequest(UploadArticleRequestDTO requestDTO) {
        validRequests.add(requestDTO);
    }

    public void addInvalidRequest(UploadArticleRequestDTO requestDTO) {
        invalidRequests.add(requestDTO);
    }

    public int getTotalCount() {
        return invalidRequests.size() + validRequests.size();
    }
}
