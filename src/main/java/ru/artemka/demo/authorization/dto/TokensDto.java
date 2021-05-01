package ru.artemka.demo.authorization.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TokensDto {
    private final String jwt;

    private final String refreshToken;
}
