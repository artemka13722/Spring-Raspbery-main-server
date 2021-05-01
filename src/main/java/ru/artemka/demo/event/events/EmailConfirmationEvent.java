package ru.artemka.demo.event.events;

import org.springframework.context.ApplicationEvent;
import ru.artemka.demo.model.User;

public class EmailConfirmationEvent extends ApplicationEvent {

    public EmailConfirmationEvent(User user) {
        super(user);
    }

    public User getUser() {
        return (User) super.source;
    }
}
