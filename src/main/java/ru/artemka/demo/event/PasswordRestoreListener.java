package ru.artemka.demo.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.artemka.demo.authorization.services.TokenService;
import ru.artemka.demo.email.EmailSenderService;
import ru.artemka.demo.event.events.PasswordRestoreEvent;
import ru.artemka.demo.model.User;
import ru.artemka.demo.model.token.PasswordRestoreToken;
import ru.artemka.demo.model.token.Token;
import ru.artemka.demo.repository.PasswordRestoreTokenRepository;
import ru.artemka.demo.utils.AddressProperties;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordRestoreListener {

    private final PasswordRestoreTokenRepository restoreRepository;

    private final TokenService tokenService;

    private final EmailSenderService emailSenderService;

    @EventListener
    public void onPasswordRestoreListener(PasswordRestoreEvent event) {
        User user = event.getUser();
        try {
            Token token = tokenService.fillToken(restoreRepository, user.getId(), new PasswordRestoreToken());
            String message = "<body>Здравствуйте, " + user.getName()
                    + "!\nВаш токен для восстановления пароля: " + token.getStringToken() + "\n</body>";
            emailSenderService.sendHtmlEmailToUser(user.getEmail(), "Восстановление пароля", message);
        } catch (Exception exc) {
            log.error("Error sending password restore link to user: {}", user.getEmail(), exc);
        }
    }
}
