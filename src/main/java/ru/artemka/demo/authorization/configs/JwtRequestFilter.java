package ru.artemka.demo.authorization.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.artemka.demo.model.User;
import ru.artemka.demo.authorization.services.JwtTokenProvider;
import ru.artemka.demo.authorization.services.ProfileService;
import ru.artemka.demo.authorization.services.RefreshTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    @Lazy
    private final ProfileService profileService;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        String jwtToken = jwtTokenProvider.getTokenFromRequest(request);
        String username = jwtTokenProvider.getUsername(jwtToken);
        String refreshTokenId = jwtTokenProvider.getRefreshTokenId(jwtToken);

        if (username == null) {
            securityContext.setAuthentication(null);
        } else if (isLoginNecessary(username)) {
            refreshTokenProvider.validateTokenById(Integer.parseInt(refreshTokenId));
            User user = (User) profileService.loadUserByUsername(username);
            if (jwtTokenProvider.validateToken(jwtToken, user)) {
                securityContext.setAuthentication(getAuthentication(user));
            }
        }

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    private boolean isLoginNecessary(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || !username.equals(authentication.getName());
    }
}
