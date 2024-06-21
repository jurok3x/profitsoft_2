package com.ykotsiuba.profitsoft_2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessageDto {

    private String to;

    private String subject;

    private String content;
}
