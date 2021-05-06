package ru.artemka.demo.hub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.artemka.demo.hub.dto.HubAddDto;
import ru.artemka.demo.hub.dto.HubDeleteDto;
import ru.artemka.demo.hub.dto.HubDto;
import ru.artemka.demo.hub.dto.HubSettingsDto;
import ru.artemka.demo.hub.services.HubService;
import ru.artemka.demo.utils.paths.ApiVersionConstants;
import ru.artemka.demo.utils.paths.HubPaths;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(ApiVersionConstants.API_VERSION_PREFIX_1)
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping(HubPaths.ADD_HUB)
    public void addHub(@ApiIgnore Principal principal, HubAddDto hubAddDto) {
        hubService.addHub(principal, hubAddDto);
    }

    @GetMapping(HubPaths.GET_ALL_HUBS)
    public List<HubDto> getAllHubs(@ApiIgnore Principal principal) {
        return hubService.getAllHubs(principal);
    }

    @PostMapping(HubPaths.SET_HUB_PIN_SETTINGS)
    public void setHubPortSettings(@ApiIgnore Principal principal,
                                   @RequestBody HubSettingsDto hubSettingsDto) {
        hubService.setHubPortSettings(principal, hubSettingsDto);
    }

    @DeleteMapping(HubPaths.DELETE_HUB_PIN_SETTINGS)
    public void deleteHubPortSettings(@ApiIgnore Principal principal, @RequestBody HubDeleteDto hubDeleteDto) {
        hubService.deleteHubPortSettings(principal, hubDeleteDto);
    }
}
