package ru.artemka.demo.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.artemka.demo.authorization.services.TokenService;
import ru.artemka.demo.email.EmailSenderService;
import ru.artemka.demo.event.events.EmailConfirmationEvent;
import ru.artemka.demo.model.User;
import ru.artemka.demo.model.token.ConfirmationToken;
import ru.artemka.demo.model.token.Token;
import ru.artemka.demo.repository.ConfirmationTokenRepository;
import ru.artemka.demo.utils.paths.ApiVersionConstants;
import ru.artemka.demo.utils.paths.AuthorizationPaths;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConfirmationListener {

    private final EmailSenderService emailSenderService;

    private final TokenService tokenService;

    private final ConfirmationTokenRepository confirmationTokenRepository;


    // TODO: 01.05.2021 изменить заглушки
    @EventListener
    public void onApplicationEvent(EmailConfirmationEvent event) {
        User user = event.getUser();
        String domainName = "localhost";
        int port = 8080;

        try {
            Token token = tokenService.fillToken(confirmationTokenRepository, user.getId(), new ConfirmationToken());
            String confirmUrl = "https://" + domainName + ":" + port
                    + ApiVersionConstants.API_VERSION_PREFIX_1
                    + AuthorizationPaths.EMAIL_CONFIRM + "?token=" + token.getStringToken();
            String message = "<body>Здравствуйте, " + user.getName()
                    + "!\nНажмите на <a href = \"" + confirmUrl + "\">ссылку</a>, " +
                    "чтобы подтвердить вашу электронную почту.\n</body>";
            emailSenderService.sendHtmlEmailToUser(user.getEmail(), "Подтверждение почты", message);
        } catch (Exception exc) {
            log.error("Error sending email confirmation link to user: {}", user.getEmail(), exc);
        }
    }
}
