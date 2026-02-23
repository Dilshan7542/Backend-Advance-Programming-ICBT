package lk.icbt.resort.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {
    private PasswordUtil() {}

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Matches either: (1) plain stored password, or (2) SHA-256 stored password.
     */
    public static boolean matches(String rawPassword, String storedPasswordHashOrPlain) {
        if (rawPassword == null || storedPasswordHashOrPlain == null) return false;
        if (storedPasswordHashOrPlain.equals(rawPassword)) return true; // legacy/plain
        return storedPasswordHashOrPlain.equalsIgnoreCase(sha256(rawPassword));
    }
}
