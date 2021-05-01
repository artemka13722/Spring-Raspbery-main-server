package ru.artemka.demo.authorization.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemka.demo.authorization.dto.Credentials;
import ru.artemka.demo.authorization.dto.TokensDto;
import ru.artemka.demo.authorization.dto.UserRegistrationDto;
import ru.artemka.demo.event.events.EmailConfirmationEvent;
import ru.artemka.demo.exception.DataNotFoundException;
import ru.artemka.demo.exception.UnconfirmedEmailException;
import ru.artemka.demo.model.Role;
import ru.artemka.demo.model.RoleEntity;
import ru.artemka.demo.model.User;
import ru.artemka.demo.model.token.RefreshToken;
import ru.artemka.demo.repository.ConfirmationTokenRepository;
import ru.artemka.demo.repository.RoleRepository;
import ru.artemka.demo.repository.UserRepository;
import ru.artemka.demo.services.UserRolesService;
import ru.artemka.demo.utils.MutableSetBuilder;
import ru.artemka.demo.utils.dtoTransform.UserDtoTransformService;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final UserDtoTransformService userDtoTransformService;

    @Lazy
    private final AuthenticationManager authenticationManager;

    private final RefreshTokenProvider refreshTokenProvider;

    private final AuthorizationTokenProvider authorizationTokenProvider;

    private final UserRolesService userRolesService;

    private final ApplicationEventPublisher eventPublisher;

    private final TokenService tokenService;

    private final RoleRepository roleRepository;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public User getUserById(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("WrongUserId"));
    }

    @Transactional
    public void addUser(UserRegistrationDto userRegistrationDto) {
        checkUser(userRegistrationDto.getName());
        Set<Role> roles = MutableSetBuilder.build(Role.UNCONFIRMED);

        User user = userDtoTransformService.convertToUser(userRegistrationDto);
        String plainPassword = userRegistrationDto.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(plainPassword));
        userRolesService.addRoles(user, roles);

        userRepository.save(user);
        eventPublisher.publishEvent(new EmailConfirmationEvent(user));
        log.info("User [{}] was registered", user.getName());
    }

    public void checkUser(String email) {
        User user = userRepository.findUserByEmailIgnoreCase(email);
        if (user != null) {
            throw new IllegalArgumentException("Данный логин уже зарегестрирован");
        }
    }

    @Transactional
    public TokensDto generateAuthorizationTokens(Credentials credentials) {
        User user = authenticateUser(credentials);

        if (!isUserConfirmed(user)) {
            throw new UnconfirmedEmailException("Unconfirmed");
        }

        log.info("User {} logged in", credentials.getEmail());
        RefreshToken refreshToken = refreshTokenProvider.generateRefreshToken(user.getId());

        String jwt = authorizationTokenProvider.generateToken(credentials.getEmail(), refreshToken.getId());
        return new TokensDto(jwt, refreshToken.getStringToken());
    }

    private User authenticateUser(Credentials credentials) {
        String login = credentials.getEmail();
        String password = credentials.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        return userRepository.findUserByNameIgnoreCase(login);
    }

    private boolean isUserConfirmed(User user) {
        return user.getRoles().stream().noneMatch(r -> r.getRoleName() == Role.UNCONFIRMED);
    }

    @Transactional
    public void confirmEmail(String token) {
        int userId = tokenService.getUserIdFromToken(confirmationTokenRepository, token);
        User user = getUserById(userId);

        RoleEntity unconfirmedEntity = getRoleEntity(Role.UNCONFIRMED);
        Set<RoleEntity> roles = user.getRoles();

        roles.remove(unconfirmedEntity);
        roles.add(getRoleEntity(Role.USER));
        log.info("User {} confirmed email", user.getEmail());
    }

    public RoleEntity getRoleEntity(Role role) {
        return roleRepository.findByRoleName(role)
                .orElseThrow(() -> new DataNotFoundException("Wrong role"));
    }
}
