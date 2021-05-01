package ru.artemka.demo.authorization.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemka.demo.model.token.RefreshToken;
import ru.artemka.demo.repository.RefreshTokenRepository;
import ru.artemka.demo.utils.SecureRandomSequenceGenerator;

import java.time.Clock;
import java.time.Duration;

@Service
public class RefreshTokenProvider extends TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenProvider(RefreshTokenRepository refreshTokenRepository,
                                @Value("${refresh.expiration}") Duration duration,
                                Clock clock,
                                SecureRandomSequenceGenerator randomSequenceGenerator) {
        super(duration, clock, randomSequenceGenerator);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken generateRefreshToken(int userId) {
        return fillToken(refreshTokenRepository, userId, new RefreshToken());
    }

    public void validateTokenById(int id) {
        RefreshToken refreshToken = refreshTokenRepository.findById(id).orElse(null);
        validateToken(refreshToken);
    }

    @Transactional
    public RefreshToken updateToken(String stringToken) {
        RefreshToken oldRefreshToken = refreshTokenRepository.findByStringToken(stringToken);
        validateToken(oldRefreshToken);
        refreshTokenRepository.deleteByStringToken(stringToken);
        return generateRefreshToken(oldRefreshToken.getUserId());
    }

    public void deleteTokenByUserId(int userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public void deleteOtherUserTokens(int userId, String token) {
        refreshTokenRepository.deleteByUserIdAndStringTokenNot(userId, token);
    }
}
