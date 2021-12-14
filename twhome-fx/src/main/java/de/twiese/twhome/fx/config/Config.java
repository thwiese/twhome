package de.twiese.twhome.fx.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

public class Config {

    private final String configLocation;
    private final Properties defaultProperties;
    private final Properties properties;

    public Config(String configLocation) throws IOException {
        this.configLocation = configLocation;
        this.defaultProperties = new Properties();
        this.properties = new Properties();
        defaultProperties.load(getClass().getClassLoader().getResourceAsStream("default-config.properties"));
        reload();
    }

    public void reload() throws IOException {
        if (configLocation != null && configLocation.length() > 0) {
            if (configLocation.startsWith("classpath:")) {
                System.out.println("Load configuration from classpath: " + configLocation);
                properties.load(getClass().getClassLoader().getResourceAsStream(configLocation.replace("classpath:", "")));
            } else if (configLocation.startsWith("http://") || configLocation.startsWith(("https://"))) {
                throw new UnsupportedOperationException("Load configuration by http is not yet implemented");
            } else {
                System.out.println("Load configuration from file: " + configLocation);
                properties.load(new FileInputStream(configLocation));
            }
        } else {
            System.out.println("No configLocation is set. Using default configuration.");
        }

        TimeZone.setDefault(TimeZone.getTimeZone(getProperty("timezone")));
        Locale.setDefault(new Locale(getProperty("localeLanguage"), getProperty("localeCountry")));
    }

    public String getProperty(String key) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        } else {
            return defaultProperties.getProperty(key);
        }
    }

}
