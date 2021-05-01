package ru.artemka.demo.model.token;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RefreshTokens")
public class RefreshToken extends Token {
}
