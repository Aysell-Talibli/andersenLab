package org.example.BookStore;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;

    public ConfigLoader(String configFilePath) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOrderFileLocation() {
        return properties.getProperty("orders.file.location", "orders.txt");
    }

    public boolean isBookAvailabilityChangeEnabled() {
        return Boolean.parseBoolean(properties.getProperty("book.availability.change.enabled", "true"));
    }
}
