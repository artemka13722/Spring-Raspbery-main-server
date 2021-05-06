package ru.artemka.demo.authorization.dto;

import lombok.Data;
import ru.artemka.demo.utils.RegexTemplateConstants;

import javax.validation.constraints.Pattern;

@Data
public class NewPasswordDto {
    @Pattern(regexp = RegexTemplateConstants.PASSWORD, message = "Некорректное поле 'Новый пароль'")
    private String newPassword;
}
