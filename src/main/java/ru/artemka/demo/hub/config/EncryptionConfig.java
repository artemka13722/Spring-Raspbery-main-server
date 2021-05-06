package ru.artemka.demo.hub.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.ZeroSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {
    @Bean
    public StringEncryptor stringEncryptor(@Value("${secret.key}") String tokenKey) {
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setSaltGenerator(new ZeroSaltGenerator());
        stringEncryptor.setPassword(tokenKey);
        stringEncryptor.initialize();
        return stringEncryptor;
    }
}
