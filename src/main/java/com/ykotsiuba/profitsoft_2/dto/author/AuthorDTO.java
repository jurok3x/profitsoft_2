package com.ykotsiuba.profitsoft_2.dto.author;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {

    private UUID id;

    private String email;

    private String firstName;

    private String lastName;
}
