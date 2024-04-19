package com.ykotsiuba.profitsoft_2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadArticlesResponseDTO {
    private Integer uploaded;
    private Integer errors;
}
