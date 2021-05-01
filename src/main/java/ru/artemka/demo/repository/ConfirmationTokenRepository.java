package ru.artemka.demo.repository;

import org.springframework.stereotype.Repository;
import ru.artemka.demo.model.token.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository extends TokenRepository<ConfirmationToken> {

    void deleteAllByUserId(int userId);
}
