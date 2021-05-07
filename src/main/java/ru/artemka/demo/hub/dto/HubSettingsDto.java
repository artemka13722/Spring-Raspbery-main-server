package ru.artemka.demo.hub.dto;

import lombok.Data;

@Data
public class HubSettingsDto {
    private int hubId;

    private HubPins pin;

    private HubSensor sensor;

    private HubScripts scripts;

    private HubPins relayMotionPin;

    private Integer timeDelay;
}
