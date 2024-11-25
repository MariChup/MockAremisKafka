package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    public static Properties loadConfig() {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file: " + CONFIG_FILE_PATH, e);
        }

        return properties;
    }
}
