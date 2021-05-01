package ru.artemka.demo.model.token;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ConfirmationTokens")
public class ConfirmationToken extends Token {
}
