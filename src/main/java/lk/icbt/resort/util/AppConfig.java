package lk.icbt.resort.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    private AppConfig() {
    }

    public static String get(String key) {
        return PROPS.getProperty(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }
}
