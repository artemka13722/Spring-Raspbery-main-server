package ru.artemka.demo.hub.dto;

import lombok.Data;

@Data
public class HubSettingsDto {
    private int hubId;

    private String pin;

    private String sensor;

    private String scripts;

    private String relayMotionPin;

    private Integer timeDelay;
}
