package ru.artemka.demo.authorization.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemka.demo.model.token.Token;
import ru.artemka.demo.exception.BadTokenException;
import ru.artemka.demo.repository.TokenRepository;
import ru.artemka.demo.utils.SecureRandomSequenceGenerator;

import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    protected final Clock clock;

    @DurationUnit(ChronoUnit.DAYS)
    protected final Duration duration;

    protected final SecureRandomSequenceGenerator randomSequenceGenerator;

    public TokenService(@Value("${token.expiration}") Duration duration,
                        Clock clock,
                        SecureRandomSequenceGenerator randomSequenceGenerator) {
        this.clock = clock;
        this.duration = duration;
        this.randomSequenceGenerator = randomSequenceGenerator;
    }

    @Transactional
    public <T extends Token> T fillToken(TokenRepository<T> tokenRepository, int userId, T token) {
        token.setUserId(userId);
        token.setExpirationDate(ZonedDateTime.now(clock).plus(duration));
        token.setStringToken(randomSequenceGenerator.getRandomStringSequence());

        return tokenRepository.save(token);
    }

    public int getUserIdFromToken(TokenRepository<? extends Token> tokenRepository, String stringToken) {
        Token token = tokenRepository.findByStringToken(stringToken);
        validateToken(token);
        return token.getUserId();
    }

    public void validateToken(Token token) {
        if (token == null) {
            throw new BadTokenException("Wrong token");
        }

        if (isExpired(token)) {
            throw new BadTokenException("Token was expired");
        }
    }

    public boolean isExpired(Token token) {
        return token.getExpirationDate().isBefore(ZonedDateTime.now(clock));
    }
}
