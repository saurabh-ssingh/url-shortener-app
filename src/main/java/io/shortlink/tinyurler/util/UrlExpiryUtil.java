package io.shortlink.tinyurler.util;

import java.time.LocalDateTime;

public class UrlExpiryUtil {

    private UrlExpiryUtil() {} // prevent instantiation

    public static boolean isExpired(LocalDateTime expiresAt) {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }
}