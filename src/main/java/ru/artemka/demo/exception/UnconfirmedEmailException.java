package ru.artemka.demo.exception;

public class UnconfirmedEmailException extends RuntimeException {
    public UnconfirmedEmailException(String message) {
        super(message);
    }
}
