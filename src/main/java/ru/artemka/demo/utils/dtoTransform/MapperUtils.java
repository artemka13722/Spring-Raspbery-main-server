package ru.artemka.demo.utils.dtoTransform;


import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapperUtils {
    private static final String DEFAULT_DELIMITER = "\n";

    private final ModelMapper modelMapper;

    public <S, D> D simpleMap(S source, Class<D> targetClass) {
        return source == null ? null : modelMapper.map(source, targetClass);
    }

    public <S, D> Converter<S, D> getSimpleConverter(Class<D> targetClass) {
        return context -> simpleMap(context.getSource(), targetClass);
    }

    public static Converter<Iterable<String>, String> getStringListToStringConverter() {
        return context -> toString(context.getSource(), DEFAULT_DELIMITER);
    }

    public static String toString(Iterable<String> stringList, String delimiter) {
        return stringList == null ? null : String.join(delimiter, stringList);
    }

    public static String toString(Iterable<String> stringList) {
        return toString(stringList, DEFAULT_DELIMITER);
    }
}