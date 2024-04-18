package com.ykotsiuba.profitsoft_2.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthorDTO {
    private UUID id;
    private String firstName;
    private String lastName;
}
