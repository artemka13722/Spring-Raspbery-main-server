package ru.artemka.demo.model.token;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Table(name = "Tokens")
@MappedSuperclass
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String stringToken;

    private int userId;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime expirationDate;
}
