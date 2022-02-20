package ua.nure.bulhakov.processing;

import ua.nure.bulhakov.util.ApplicationProperties;

import java.io.*;
import java.math.BigDecimal;
import java.util.Map;

public class ResultTransformer {

    private static final String FILENAME_SUFFIX = "_statistics.txt";

    private final ApplicationProperties applicationProperties;

    public ResultTransformer(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void transform(Map<String, Map<String, Map<Character, BigDecimal>>> filesByDirectory) {
        File targetDirectory = getTargetDirectory();
        filesByDirectory.forEach((key, value) -> transformDirectory(key, targetDirectory, value));
    }

    private File getTargetDirectory() {
        String targetDirectoryName = applicationProperties.getStringProperty(ApplicationProperties.Property.TARGET_DIRECTORY);
        File targetDirectory = new File(targetDirectoryName);
        if (targetDirectory.exists()) {
            targetDirectory.delete();
        }
        targetDirectory.mkdir();
        return targetDirectory;
    }

    private void transformDirectory(String directoryName, File parent, Map<String, Map<Character, BigDecimal>> symbolsByFile) {
        File directory = new File(parent, directoryName);
        directory.mkdir();
        symbolsByFile.forEach((key, value) -> transformFile(key, directory, value));
    }

    private void transformFile(String filename, File parent, Map<Character, BigDecimal> statistics) {
        String filenamePrefix = filename.substring(0, filename.lastIndexOf("."));
        File statisticsFile = new File(parent, filenamePrefix + FILENAME_SUFFIX);
        try {
            statisticsFile.createNewFile();
            writeStatistics(statisticsFile, statistics);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void writeStatistics(File statisticsFile, Map<Character, BigDecimal> statistics) {
        try (FileWriter writer = new FileWriter(statisticsFile)) {
            statistics.forEach((key, value) -> {
                String statisticsString = String.format("%c %s%n", key, value.toString());
                try {
                    writer.write(statisticsString);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
