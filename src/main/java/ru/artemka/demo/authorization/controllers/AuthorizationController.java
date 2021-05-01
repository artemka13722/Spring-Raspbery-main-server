package ru.artemka.demo.authorization.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.artemka.demo.authorization.dto.Credentials;
import ru.artemka.demo.authorization.dto.TokensDto;
import ru.artemka.demo.authorization.dto.UserRegistrationDto;
import ru.artemka.demo.authorization.services.AuthorizationService;
import ru.artemka.demo.utils.paths.ApiVersionConstants;
import ru.artemka.demo.utils.paths.AuthorizationPaths;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiVersionConstants.API_VERSION_PREFIX_1)
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping(AuthorizationPaths.SIGN_UP)
    @ApiOperation("Регистрация")
    public void signUp(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        authorizationService.addUser(userRegistrationDto);
    }

    @ApiOperation("Вход в систему")
    @PostMapping(AuthorizationPaths.SIGN_IN)
    public TokensDto signIn(@RequestBody @Valid Credentials credentials) {
        return authorizationService.generateAuthorizationTokens(credentials);
    }

    @GetMapping(AuthorizationPaths.EMAIL_CONFIRM)
    @ApiOperation("Подтверждение почты")
    public void confirmEmail(@RequestParam String token) {
        authorizationService.confirmEmail(token);
    }
}
