package dev.albertv.projects.api.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public final class HashUtils {

    public String sha256Hex(final String message) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return HexUtils.toHexString(messageDigest.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
