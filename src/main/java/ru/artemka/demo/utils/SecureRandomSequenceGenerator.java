package ru.artemka.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@Slf4j
public class SecureRandomSequenceGenerator {
    private static final int DEFAULT_SEQUENCE_LENGTH = 16;

    private static final String NON_BLOCKING_RANDOM_ALGORITHM = "NativePRNGNonBlocking";

    private static SecureRandom secureRandom;

    public SecureRandomSequenceGenerator() {
        try {
            // поддерживается на большинстве UNIX-машин
            secureRandom = SecureRandom.getInstance(NON_BLOCKING_RANDOM_ALGORITHM);
        } catch (NoSuchAlgorithmException exception) {
            log.error("Error during secure random initialization: {}", exception.getLocalizedMessage());
            // на windows по дефолту стоит SHA1PRNG, который не блокируется для выдачи следующих рандомных байт
            secureRandom = new SecureRandom();
            log.info("Secure random has been set to default implementation");
        }
    }

    /**
     * Получение случайной последовательности байтов длины sequenceLength
     */
    public byte[] getRandomSequence(int sequenceLength) {
        byte[] sequence = new byte[sequenceLength];
        secureRandom.nextBytes(sequence);
        return sequence;
    }

    /**
     * Получение случайной строки длины sequenceLength
     */
    public String getRandomStringSequence(int sequenceLength) {
        byte[] base64bytes = Base64.getUrlEncoder().encode(getRandomSequence(sequenceLength));
        return new String(base64bytes, StandardCharsets.UTF_8);
    }

    /**
     * Получение случайной строки длины DEFAULT_SEQUENCE_LENGTH = 16
     */
    public String getRandomStringSequence() {
        return getRandomStringSequence(DEFAULT_SEQUENCE_LENGTH);
    }

    public String getRandomIntSequence() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(secureRandom.nextInt(10));
        }
        return code.toString();
    }
}
