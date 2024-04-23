package com.ykotsiuba.profitsoft_2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class APIException {

    private String message;

    private HttpStatus httpStatus;

    private LocalDateTime timestamp;

    private List<String> errors;
}
