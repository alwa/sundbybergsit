package com.sundbybergsit.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoader {

    private static Properties prop;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        prop = new Properties();
        String localProperty = System.getProperty("local");
        String propertiesFile = localProperty == null ? "configuration.properties" : "configuration_local.properties";
        InputStream in = ConfigurationLoader.class
                .getResourceAsStream(propertiesFile);
        try {
            prop.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseURL() {
        return prop.getProperty("host.url");
    }
}