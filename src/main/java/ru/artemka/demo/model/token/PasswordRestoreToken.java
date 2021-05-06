package ru.artemka.demo.model.token;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PasswordRestoreTokens")
public class PasswordRestoreToken extends Token {
}

