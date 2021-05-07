package ru.artemka.demo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HubScenariesDto {
    private HubScripts scripts;
    private String name;
    private HubSensor sensor;
}
