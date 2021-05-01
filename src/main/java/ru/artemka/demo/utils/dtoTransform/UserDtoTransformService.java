package ru.artemka.demo.utils.dtoTransform;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemka.demo.model.User;
import ru.artemka.demo.authorization.dto.UserRegistrationDto;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class UserDtoTransformService {
    private final ModelMapper modelMapper;

    private final MapperUtils mapperUtils;

    @Transactional
    public User convertToUser(UserRegistrationDto userRegistrationDto) {
        return modelMapper.map(userRegistrationDto, User.class);
    }

    @PostConstruct
    private void configureMapper() {
        modelMapper.createTypeMap(UserRegistrationDto.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setPassword);
                });
    }
}
