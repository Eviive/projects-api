package dev.albertv.projects.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public final class HashUtils {

    public String sha256Hex(String message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return hex(messageDigest.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private String hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(
                Integer.toHexString((b & 0xFF) | 0x100),
                1,
                3
            );
        }
        return stringBuilder.toString();
    }

}
