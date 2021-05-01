package ru.artemka.demo.authorization.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Credentials {
    @ApiModelProperty(value = "Email опьзователя", example = "tester@ya.ru")
    private String email;

    @ApiModelProperty(value = "Пароль", example = "SuperSecret123")
    private String password;
}
