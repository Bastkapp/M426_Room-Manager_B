package ch.roomManager.service;

import javax.ws.rs.ApplicationPath;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * configure the web services and properties
 * <p>
 * M151: BookDB
 *
 * @author Marcel Suter (Ghwalin)
 */

@ApplicationPath("/resource")
public class Config {
    private static final URL PROPERTIES_PATH = Config.class.getClassLoader().getResource("roomManager.properties");
    private static Properties properties = null;

    /**
     * Gets the value of a property
     *
     * @param property the key of the property to be read
     * @return the value of the property
     */

    public static String getProperty(String property) {
        if (Config.properties == null) {
            setProperties(new Properties());
            readProperties();
        }
        String value = Config.properties.getProperty(property);
        if (value == null) return "";
        return value;
    }

    /**
     * reads the properties file
     */
    private static void readProperties() {

        InputStream inputStream = null;
        try {
            inputStream = PROPERTIES_PATH.openStream();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {

            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

        }

    }

    /**
     * Sets the properties
     *
     * @param properties the value to set
     */

    private static void setProperties(Properties properties) {
        Config.properties = properties;
    }
}