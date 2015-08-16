package com.sundbybergsit.applet;


import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translator {

//    private static final Logger LOGGER = LoggerFactory.getLogger(Translator.class);
    private static ResourceBundle bundle = null;

    public static String localise(String key) {
        return key;
        /*
        if (bundle == null) {
            initialize();
        }

        if (key == null) {
            throw new IllegalArgumentException("Key must not be null!");
        }

        if (key.length() == 0) {
            return key;
        }

        try {
            return bundle.getString(key);
        }
        catch (MissingResourceException e) {
//            LOGGER.warn("No translation for key: " + key);
            return key;
        }
        */
    }


    public static String localise(String key, Object... arguments) {
        return key;
        /*
        String result = localise(key);
        if (arguments == null || arguments.length == 0) {
            return result;
        }
        return MessageFormat.format(result, arguments);
        */
    }


    private static synchronized void initialize() {
        //bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
    }
}
