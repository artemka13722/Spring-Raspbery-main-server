package ru.artemka.demo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.artemka.demo.exception.BadTokenException;
import ru.artemka.demo.exception.DataNotFoundException;
import ru.artemka.demo.exception.HubException;
import ru.artemka.demo.exception.UnconfirmedEmailException;

@ControllerAdvice
public class BaseExceptionHandler {
    @RequiredArgsConstructor
    @Getter
    public final static class ErrorModel {
        private final String errorMessage;
    }

    @ExceptionHandler(BadTokenException.class)
    public ResponseEntity<ErrorModel> handleBadToken(BadTokenException exception) {
        return new ResponseEntity<>(new ErrorModel(exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorModel> handleDataIsWrongException(DataNotFoundException ex) {
        return new ResponseEntity<>(new ErrorModel(ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnconfirmedEmailException.class)
    public ResponseEntity<ErrorModel> handleDataIsWrongException(UnconfirmedEmailException ex) {
        return new ResponseEntity<>(new ErrorModel(ex.getLocalizedMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HubException.class)
    public ResponseEntity<ErrorModel> handleHubException(HubException ex) {
        return new ResponseEntity<>(new ErrorModel(ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }
}
