package com.utils;

import java.io.*;
import java.util.Properties;

public class PropertyReader {
    protected final static Properties prop = new Properties();

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    public static void readPropertiesFile() {
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream("resources.properties")) {
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            try {
                prop.load(reader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }
}