package ru.artemka.demo.hub.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artemka.demo.exception.HubException;
import ru.artemka.demo.hub.dto.HubDeleteDto;
import ru.artemka.demo.hub.dto.HubSettingsDto;
import ru.artemka.demo.model.Hub;
import ru.artemka.demo.repository.HubRepository;

@Service
@RequiredArgsConstructor

// TODO: 06.05.2021 добавить обработчик ошибок
public class HubClient {

    private final HubRepository hubRepository;

    public void setHubPortConfig(HubSettingsDto hubPortConfig) {
        Hub hub = hubRepository.findById(hubPortConfig.getHubId()).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("http://" + hub.getAddress() + "/gpio-controller/set-pin")
                    .header("Content-Type", "application/json")
                    .body("{\n  \"pin\": \"" + hubPortConfig.getPin() +
                            "\",\n  \"relayMotionPin\": \"" + hubPortConfig.getRelayMotionPin() +
                            "\",\n  \"scripts\": \"" + hubPortConfig.getScripts() +
                            "\",\n  \"sensor\": \"" + hubPortConfig.getSensor() +
                            "\",\n  \"timeDelay\": " + hubPortConfig.getTimeDelay() + "\n}")
                    .asString();
        } catch (UnirestException e) {
            throw new HubException("Ошибка сохранения настроек");
        }
    }

    public void deleteHubPortConfig(HubDeleteDto hubDeleteDto) {
        Hub hub = hubRepository.findById(hubDeleteDto.getHubId()).orElseThrow(() -> new HubException("Wrong hub id"));
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.delete("http:/" + hub.getAddress() + "/gpio-controller/unset-pin")
                    .header("Content-Type", "application/json")
                    .body("{\n  \"pin\": \"" + hubDeleteDto.getPin() + "\"\n}")
                    .asString();
        } catch (UnirestException e) {
            throw new HubException("Ошибка удаления настроек");
        }
    }

}
