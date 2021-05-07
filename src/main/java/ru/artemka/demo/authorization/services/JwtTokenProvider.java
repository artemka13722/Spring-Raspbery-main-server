package ru.artemka.demo.authorization.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;
import ru.artemka.demo.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider implements AuthorizationTokenProvider {
    private static final String AUTH_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    private final Clock clock;

    private final String secret;

    @DurationUnit(ChronoUnit.HOURS)
    @Setter
    private Duration duration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") Duration duration,
                            Clock clock) {
        this.secret = secret;
        this.clock = clock;
        this.duration = duration;
    }

    @Override
    public String getUsername(String token) {
        return token == null ? null : getClaim(token, Claims::getSubject);
    }

    public String getRefreshTokenId(String token) {
        return token == null ? null : getClaim(token, Claims::getId);
    }

    @Override
    public Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date(clock.millis()));
    }

    @Override
    public String generateToken(String login, int refreshTokenId) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(login)
                .setId(String.valueOf(refreshTokenId))
                .setIssuedAt(new Date(clock.millis()))
                .setExpiration(new Date(clock.millis() + duration.toMillis()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public boolean validateToken(String token, User user) {
        String username = getUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }
}
