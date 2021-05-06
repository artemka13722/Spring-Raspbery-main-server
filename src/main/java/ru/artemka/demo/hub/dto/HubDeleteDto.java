package ru.artemka.demo.hub.dto;

import lombok.Data;

@Data
public class HubDeleteDto {
    private int hubId;

    private String pin;
}
