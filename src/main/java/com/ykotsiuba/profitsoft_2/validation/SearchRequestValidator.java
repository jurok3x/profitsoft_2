package com.ykotsiuba.profitsoft_2.validation;

import com.ykotsiuba.profitsoft_2.dto.SearchArticleRequestDTO;
import com.ykotsiuba.profitsoft_2.validation.annotation.ValidSearchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SearchRequestValidator implements ConstraintValidator<ValidSearchRequest, SearchArticleRequestDTO> {

    /**
     * Checks if at least one search parameter is present.
     * @param requestDTO
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(SearchArticleRequestDTO requestDTO, ConstraintValidatorContext constraintValidatorContext) {
        return requestDTO != null &&
                (requestDTO.getAuthorId() != null ||
                        requestDTO.getField() != null ||
                        requestDTO.getJournal() != null ||
                        requestDTO.getYear() != null ||
                        requestDTO.getTitlePart() != null);
    }
}
