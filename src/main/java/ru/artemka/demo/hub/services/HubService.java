package ru.artemka.demo.hub.services;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import ru.artemka.demo.authorization.services.AuthorizationService;
import ru.artemka.demo.exception.HubException;
import ru.artemka.demo.hub.client.HubClient;
import ru.artemka.demo.hub.dto.*;
import ru.artemka.demo.hub.mapper.HubDtoTransformService;
import ru.artemka.demo.model.Hub;
import ru.artemka.demo.model.User;
import ru.artemka.demo.repository.HubRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubService {

    private final AuthorizationService authorizationService;

    private final HubDtoTransformService mapper;

    private final HubClient hubClient;

    private final HubRepository hubRepository;

    private final StringEncryptor stringEncryptor;

    public List<HubDto> getAllHubs(Principal principal) {
        User user = authorizationService.getUserWithCheck(principal);
        return user.getHubs().stream()
                .map(mapper::getHubDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addHub(Principal principal, HubAddDto hubAddDto) {
        User user = authorizationService.getUserWithCheck(principal);

        String encodedToken = new String(Base64.getUrlDecoder().decode(hubAddDto.getKey()));
        String address = stringEncryptor.decrypt(encodedToken);

        if(hubRepository.findHubByAddress(address).isPresent()) {
            throw new HubException("Данный адресс уже занят");
        }

        Hub hub = new Hub();
        hub.setAddress(address);
        hub.setName(hubAddDto.getName());
        hub.setUser(user);
        hubRepository.save(hub);
    }

    public void setHubPortSettings(Principal principal, HubSettingsDto hubPortSettings) {
        if (checkHubAction(principal, hubPortSettings.getHubId())) {
            hubClient.setHubPortConfig(hubPortSettings);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }

    public void deleteHubPortSettings(Principal principal, HubPinDto hubPinDto) {
        if (checkHubAction(principal, hubPinDto.getHubId())) {
            hubClient.deleteHubPortConfig(hubPinDto);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }

    public List<HubPins> getAllHubPins(Principal principal, int hubId) {
        if (checkHubAction(principal, hubId)) {
            return hubClient.getAllPins(hubId);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }

    public List<HubSensor> getAllHubSensors(Principal principal, int hubId) {
        if (checkHubAction(principal, hubId)) {
            return hubClient.getAllSensor(hubId);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }

    public List<HubScenariesDto> getAllHubScenaries(Principal principal, int hubId) {
        if (checkHubAction(principal, hubId)) {
            return hubClient.getAllScenaries(hubId);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }

    public List<HubSettingsDto> getAllHubSettings(Principal principal, int hubId) {
        if (checkHubAction(principal, hubId)) {
            return hubClient.getAllSettings(hubId);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }

    public HubDhtDataDto getHubTempPort(Principal principal, HubPinDto hubPinDto) {
        if (checkHubAction(principal, hubPinDto.getHubId())) {
            return hubClient.getTempPort(hubPinDto);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }

    private boolean checkHubAction(Principal principal, int hubId) {
        User user = authorizationService.getUserWithCheck(principal);
        Hub hub = hubRepository.findById(hubId).orElseThrow(() -> new HubException("Wrong hub Id"));
        return user.getHubs().contains(hub);
    }
}
