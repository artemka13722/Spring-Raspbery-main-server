package ru.artemka.demo.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HubScripts {
    ON("Включить рэле", HubSensor.RELAY),
    OFF("Выключить рэле", HubSensor.RELAY),
    GET_TEMP("Получить температуру и влажность", HubSensor.DHT11),
    MOTION_SENSOR_ON("Активация рэле при движении", HubSensor.MOTION_SENSOR);

    private final String name;
    private final HubSensor sensor;
}
