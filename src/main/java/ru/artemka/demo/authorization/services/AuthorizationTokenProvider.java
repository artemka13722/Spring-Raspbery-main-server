package ru.artemka.demo.authorization.services;

import org.springframework.stereotype.Component;
import ru.artemka.demo.model.User;

import java.util.Date;

@Component
public interface AuthorizationTokenProvider {
    String generateToken(String login, int refreshTokenId);

    boolean validateToken(String token, User user);

    Date getExpirationDate(String token);

    String getUsername(String token);
}
