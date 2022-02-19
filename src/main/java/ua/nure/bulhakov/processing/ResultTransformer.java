package ua.nure.bulhakov.processing;

import ua.nure.bulhakov.util.ApplicationProperties;

import java.math.BigDecimal;
import java.util.Map;

public class ResultTransformer {

    private final ApplicationProperties applicationProperties;

    public ResultTransformer(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void transform(Map<String, Map<String, Map<Character, BigDecimal>>> filesByDirectory) {
        String targetDirectory = applicationProperties.getStringProperty(ApplicationProperties.Property.TARGET_DIRECTORY);
    }
}
