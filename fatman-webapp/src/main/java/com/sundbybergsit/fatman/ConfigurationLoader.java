package com.sundbybergsit.fatman;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigurationLoader {

  // OAuth client ID
  public static String CLIENT_ID = ConfigurationLoader.getProperty("oauth_client_id");
  // OAuth client secret
  public static String CLIENT_SECRET = getProperty("oauth_client_secret");
  // Google API key
  public static String GOOGLE_API_KEY = getProperty("google_api_key");

  // Space separated list of OAuth scopes
  public static String SCOPES = getProperty("oauth_scopes");
  // OAuth redirect URI
  public static String REDIRECT_URI = getProperty("oauth_redirect_uri");

    private static final Logger log = Logger.getLogger(ConfigurationLoader.class.getName());
    private static Properties prop;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        prop = new Properties();
        InputStream in = ConfigurationLoader.class
                .getResourceAsStream("configuration.properties");
        try {
            prop.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        if (prop == null) {
            loadProperties();
        }
        if (!prop.containsKey(key) || prop.getProperty(key).trim().isEmpty()) {
            log.severe("Could not find property " + key);
            throw new RuntimeException("Could not find property " + key);
        }
        return prop.getProperty(key).trim();
    }
}