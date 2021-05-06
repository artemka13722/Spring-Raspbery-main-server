package ru.artemka.demo.hub.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.artemka.demo.hub.dto.HubDto;
import ru.artemka.demo.model.Hub;

@Service
@RequiredArgsConstructor
public class HubDtoTransformService {

    private final ModelMapper modelMapper;

    public HubDto getHubDto(Hub hub) {
        return modelMapper.map(hub, HubDto.class);
    }
}
