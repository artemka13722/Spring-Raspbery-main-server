package ru.artemka.demo.exception;

public class BadTokenException extends RuntimeException {
    public BadTokenException(String message) {
        super(message);
    }
}