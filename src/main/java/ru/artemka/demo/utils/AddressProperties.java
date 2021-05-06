package ru.artemka.demo.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AddressProperties {
    private final int port;

    private final String domainName;

    public AddressProperties(@Value("${server.port}") int port,
                             @Value("${server.domainName}") String domainName) {
        this.port = port;
        this.domainName = domainName;
    }
}
