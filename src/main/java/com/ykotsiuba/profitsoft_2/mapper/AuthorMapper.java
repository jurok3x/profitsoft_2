package com.ykotsiuba.profitsoft_2.mapper;

import com.ykotsiuba.profitsoft_2.dto.author.AuthorDTO;
import com.ykotsiuba.profitsoft_2.dto.author.SaveAuthorRequestDTO;
import com.ykotsiuba.profitsoft_2.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "articles", ignore = true)
    Author toEntity(AuthorDTO authorDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articles", ignore = true)
    Author convertSaveRequestToEntity(SaveAuthorRequestDTO requestDTO);

    AuthorDTO toDTO(Author author);
}
