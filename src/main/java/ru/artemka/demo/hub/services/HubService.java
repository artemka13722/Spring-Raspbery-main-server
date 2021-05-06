package ru.artemka.demo.hub.services;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import ru.artemka.demo.authorization.services.AuthorizationService;
import ru.artemka.demo.exception.HubException;
import ru.artemka.demo.hub.client.HubClient;
import ru.artemka.demo.hub.dto.HubAddDto;
import ru.artemka.demo.hub.dto.HubDeleteDto;
import ru.artemka.demo.hub.dto.HubDto;
import ru.artemka.demo.hub.dto.HubSettingsDto;
import ru.artemka.demo.hub.mapper.HubDtoTransformService;
import ru.artemka.demo.model.Hub;
import ru.artemka.demo.model.User;
import ru.artemka.demo.repository.HubRepository;

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
        User user = authorizationService.getUserWithCheck(principal);
        Hub hub = hubRepository.findById(hubPortSettings.getHubId()).orElseThrow(() -> new HubException("Wrong hub Id"));

        if (user.getHubs().contains(hub)) {
            hubClient.setHubPortConfig(hubPortSettings);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }

    public void deleteHubPortSettings(Principal principal, HubDeleteDto hubDeleteDto) {
        User user = authorizationService.getUserWithCheck(principal);
        Hub hub = hubRepository.findById(hubDeleteDto.getHubId()).orElseThrow(() -> new HubException("Wrong hub Id"));

        if (user.getHubs().contains(hub)) {
            hubClient.deleteHubPortConfig(hubDeleteDto);
        } else {
            throw new HubException("Wrong hub Id");
        }
    }
}
