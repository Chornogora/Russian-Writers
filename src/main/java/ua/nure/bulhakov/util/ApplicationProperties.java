package ua.nure.bulhakov.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public class ApplicationProperties {

    private static final String PROPERTIES_FILE_NAME = "src/main/resources/application.properties";
    private static final String STRING_DELIMITER = ",";

    private final Properties properties;

    public ApplicationProperties() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public int getIntProperty(Property pr) {
        return Integer.parseInt(properties.getProperty(pr.getValue()));
    }

    public String getStringProperty(Property pr) {
        return properties.getProperty(pr.getValue());
    }

    public String[] getStringArrayProperty(Property pr) {
        return properties.getProperty(pr.getValue()).split(STRING_DELIMITER);
    }

    public enum Property {
        THREADS_AMOUNT("threads-amount"),
        FILE_FOLDERS("file-folders"),
        TARGET_DIRECTORY("target-directory");

        private final String value;

        Property(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
