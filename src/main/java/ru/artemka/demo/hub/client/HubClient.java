package ru.artemka.demo.hub.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artemka.demo.exception.HubException;
import ru.artemka.demo.hub.dto.*;
import ru.artemka.demo.model.Hub;
import ru.artemka.demo.repository.HubRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

// TODO: 06.05.2021 добавить обработчик ошибок
public class HubClient {

    private final HubRepository hubRepository;

    public void setHubPortConfig(HubSettingsDto hubPortConfig) {
        Hub hub = hubRepository.findById(hubPortConfig.getHubId()).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            String body;
            if (hubPortConfig.getRelayMotionPin() != null) {
                body = "{\n  \"pin\": \"" + hubPortConfig.getPin() +
                        "\",\n  \"relayMotionPin\": \"" + hubPortConfig.getRelayMotionPin() +
                        "\",\n  \"scripts\": \"" + hubPortConfig.getScripts() +
                        "\",\n  \"sensor\": \"" + hubPortConfig.getSensor() +
                        "\",\n  \"timeDelay\": " + hubPortConfig.getTimeDelay() + "\n}";
            } else {
                body = "{\n  \"pin\": \"" + hubPortConfig.getPin() + "\",\n  \"scripts\": \"" + hubPortConfig.getScripts() +
                        "\",\n  \"sensor\": \"" + hubPortConfig.getSensor() + "\"\n}";
            }

            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("http://" + hub.getAddress() + "/gpio-controller/set-pin")
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();
        } catch (UnirestException e) {
            throw new HubException("Ошибка сохранения настроек");
        }
    }

    public void deleteHubPortConfig(HubPinDto hubPinDto) {
        Hub hub = hubRepository.findById(hubPinDto.getHubId()).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.delete("http://" + hub.getAddress() + "/gpio-controller/unset-pin")
                    .header("Content-Type", "application/json")
                    .body("{\n  \"pin\": \"" + hubPinDto.getPin() + "\"\n}")
                    .asString();
        } catch (UnirestException e) {
            throw new HubException("Ошибка удаления настроек");
        }
    }

    public List<HubPins> getAllPins(int hubId) {
        Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("http://" + hub.getAddress() + "/gpio-controller/all-pins")
                    .asString();
            return new Gson().fromJson(response.getBody(), new TypeToken<ArrayList<HubPins>>() {
            }.getType());
        } catch (UnirestException e) {
            throw new HubException("Ошибка получения пинов");
        }
    }

    public List<HubSensor> getAllSensor(int hubId) {
        Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("http://" + hub.getAddress() + "/gpio-controller/all-sensor")
                    .asString();
            return new Gson().fromJson(response.getBody(), new TypeToken<ArrayList<HubSensor>>() {
            }.getType());
        } catch (UnirestException e) {
            throw new HubException("Ошибка получения пинов");
        }
    }

    public List<HubScenariesDto> getAllScenaries(int hubId) {
        Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("http://" + hub.getAddress() + "/gpio-controller/get-all-scenaries")
                    .asString();
            return new Gson().fromJson(response.getBody(), new TypeToken<ArrayList<HubScenariesDto>>() {
            }.getType());
        } catch (UnirestException e) {
            throw new HubException("Ошибка получения сценариев");
        }
    }

    public List<HubSettingsDto> getAllSettings(int hubId) {
        Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("http://" + hub.getAddress() + "/gpio-controller/all-settings")
                    .asString();
            return new Gson().fromJson(response.getBody(), new TypeToken<ArrayList<HubSettingsDto>>() {
            }.getType());
        } catch (UnirestException e) {
            throw new HubException("Ошибка получения настроек");
        }
    }

    public HubDhtDataDto getTempPort(HubPinDto hubPinDto) {
        Hub hub = hubRepository.findById(hubPinDto.getHubId()).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("http://" + hub.getAddress() + "/dht-controller/get-value")
                    .header("Content-Type", "application/json")
                    .body("{\n  \"pin\": \"" + hubPinDto.getPin() + "\"\n}")
                    .asString();
            return new Gson().fromJson(response.getBody(), HubDhtDataDto.class);
        } catch (UnirestException e) {
            throw new HubException("Ошибка получения температуры");
        }
    }
}
