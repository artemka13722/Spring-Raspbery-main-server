package ru.artemka.demo.repository;

import ru.artemka.demo.model.token.RefreshToken;

public interface RefreshTokenRepository extends TokenRepository<RefreshToken> {
    void deleteByUserIdAndStringTokenNot(int userId, String stringToken);
}