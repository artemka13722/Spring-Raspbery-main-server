package ru.artemka.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Hubs")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"user"})
public class Hub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String address;

    @ManyToOne
    private User user;
}
