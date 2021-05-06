package ru.artemka.demo.authorization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static ru.artemka.demo.utils.RegexTemplateConstants.EMAIL;
import static ru.artemka.demo.utils.RegexTemplateConstants.PASSWORD;

@Data
@ApiModel(description = "Информация о пользователе для регистрации")
public class UserRegistrationDto {
    @ApiModelProperty(value = "Имя пользователя", required = true, example = "tester")
    private String name;

    @NotNull
    @Pattern(regexp = PASSWORD, message = "Поле 'Password' некорректно")
    @ApiModelProperty(value = "Пароль", required = true, example = "SuperSecret123")
    private String password;

    @NotNull
    @Pattern(regexp = EMAIL, message = "Поле 'Email' некорректно")
    @ApiModelProperty(value = "Электронная почта", required = true, example = "ivan_ivanich1337@gmail.com")
    private String email;
}
