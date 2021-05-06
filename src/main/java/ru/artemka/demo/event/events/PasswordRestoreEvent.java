package ru.artemka.demo.event.events;

import org.springframework.context.ApplicationEvent;
import ru.artemka.demo.model.User;

public class PasswordRestoreEvent extends ApplicationEvent {

    public PasswordRestoreEvent(User user) {
        super(user);
    }

    public User getUser() {
        return (User) super.source;
    }
}
