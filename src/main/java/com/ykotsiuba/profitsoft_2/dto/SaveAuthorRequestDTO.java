package com.ykotsiuba.profitsoft_2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveAuthorRequestDTO {
    private String firstName;
    private String lastName;
}