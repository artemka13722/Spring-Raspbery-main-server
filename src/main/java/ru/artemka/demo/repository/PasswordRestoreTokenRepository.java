package ru.artemka.demo.repository;

import org.springframework.stereotype.Repository;
import ru.artemka.demo.model.token.PasswordRestoreToken;

@Repository
public interface PasswordRestoreTokenRepository extends TokenRepository<PasswordRestoreToken> {
}
