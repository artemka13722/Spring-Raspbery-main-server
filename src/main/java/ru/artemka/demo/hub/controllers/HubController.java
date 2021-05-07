package ru.artemka.demo.hub.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.artemka.demo.hub.dto.*;
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

    @GetMapping(HubPaths.GET_ALL_HUB_PINS)
    @ApiOperation("Получение всех доступных пинов")
    public List<HubPins> getAllHubPins(@ApiIgnore Principal principal, @RequestParam int hubId) {
        return hubService.getAllHubPins(principal, hubId);
    }

    @GetMapping(HubPaths.GET_ALL_HUB_SENSOR)
    @ApiOperation("Получение всех доступных сенсоров")
    public List<HubSensor> getAllHubSensors(@ApiIgnore Principal principal, @RequestParam int hubId) {
        return hubService.getAllHubSensors(principal, hubId);
    }

    @GetMapping(HubPaths.GET_ALL_HUB_SCENARIOS)
    @ApiOperation("Получение всех доступных сценариев")
    public List<HubScenariesDto> getAllHubScenaries(@ApiIgnore Principal principal, @RequestParam int hubId) {
        return hubService.getAllHubScenaries(principal, hubId);
    }

    @GetMapping(HubPaths.GET_ALL_HUB_SETTINGS)
    @ApiOperation("Получение списка установленных настроек")
    public List<HubSettingsDto> getAllHubSettings(@ApiIgnore Principal principal, @RequestParam int hubId) {
        return hubService.getAllHubSettings(principal, hubId);
    }
}
