package com.codecool.shop.util;

import java.io.*;
import java.util.Properties;

public class ConfigurationHandler {

    private static final String CONFIGURATION_FILE_NAME = "src/main/resources/configuration.properties";

    public static void writeConfigurationProperty(String propertyName, String propertyValue) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream(CONFIGURATION_FILE_NAME);

            prop.setProperty(propertyName, propertyValue);

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String readConfigurationProperty(String propertyName) {
        Properties prop = new Properties();
        InputStream input = null;
        String propertyValue = null;

        try {

            input = new FileInputStream(CONFIGURATION_FILE_NAME);

            prop.load(input);

            propertyValue = prop.getProperty(propertyName);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return propertyValue;
    }

}
