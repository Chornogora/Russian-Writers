package ua.nure.bulhakov.processing;

import java.io.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.Callable;

public class FileProcessor implements Callable<Map<Character, BigDecimal>> {

    private final File fileToProcess;

    private final Map<Character, BigDecimal> resultMap;

    public FileProcessor(File fileToProcess, Map<Character, BigDecimal> resultMap) {
        this.fileToProcess = fileToProcess;
        this.resultMap = resultMap;
    }

    @Override
    public Map<Character, BigDecimal> call() {
        System.out.println("Processing " + fileToProcess.getName());

        try (FileReader fileReader = new FileReader(fileToProcess)) {
            int read = fileReader.read();
            while (read != -1) {
                char character = Character.toLowerCase((char) read);
                if (character >= 1072 && character < 1105) {
                    addToMap(character);
                }
                read = fileReader.read();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        System.out.println(fileToProcess.getName() + " processed");
        return resultMap;
    }

    private void addToMap(char character) {
        BigDecimal times = resultMap.get(character);
        if (times == null) {
            resultMap.put(character, new BigDecimal(1));
        } else {
            resultMap.put(character, times.add(BigDecimal.ONE));
        }
    }
}
