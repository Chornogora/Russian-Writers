package ua.nure.bulhakov;

import ua.nure.bulhakov.processing.FileProcessingManager;
import ua.nure.bulhakov.processing.ResultTransformer;
import ua.nure.bulhakov.util.ApplicationProperties;

import java.math.BigDecimal;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        FileProcessingManager fileProcessingManager = new FileProcessingManager(applicationProperties);
        ResultTransformer resultTransformer = new ResultTransformer(applicationProperties);

        Map<String, Map<String, Map<Character, BigDecimal>>> filesByDirectory = fileProcessingManager.process();
        resultTransformer.transform(filesByDirectory);
    }
}
