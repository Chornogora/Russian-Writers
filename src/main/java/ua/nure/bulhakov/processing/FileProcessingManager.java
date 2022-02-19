package ua.nure.bulhakov.processing;

import ua.nure.bulhakov.util.ApplicationProperties;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessingManager {

    private final ApplicationProperties applicationProperties;

    public FileProcessingManager(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public Map<String, Map<String, Map<Character, BigDecimal>>> process() {
        String[] fileDirectoryNames = applicationProperties.getStringArrayProperty(ApplicationProperties.Property.FILE_FOLDERS);
        Map<String, Map<String, Map<Character, BigDecimal>>> filesByDirectory = new HashMap<>();
        Stream.of(fileDirectoryNames)
                .map(File::new)
                .filter(File::exists)
                .filter(File::isDirectory)
                .filter(directory -> directory.listFiles() != null)
                .forEach(directory -> filesByDirectory.put(directory.getName(), processDirectory(directory)));
        return filesByDirectory;
    }

    private Map<String, Map<Character, BigDecimal>> processDirectory(File directory) {
        Map<String, Map<Character, BigDecimal>> charactersByFile = new HashMap<>();
        List<FileProcessor> processors = Stream.of(directory.listFiles())
                .filter(File::isFile)
                .peek(file -> charactersByFile.put(file.getName(), new HashMap<>()))
                .map(file -> new FileProcessor(file, charactersByFile.get(file.getName())))
                .collect(Collectors.toList());

        try {
            processDirectoryMultithreading(processors);
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
        return charactersByFile;
    }

    private void processDirectoryMultithreading(List<FileProcessor> processors) throws InterruptedException {
        int threadsAmount = applicationProperties.getIntProperty(ApplicationProperties.Property.THREADS_AMOUNT);
        ExecutorService threadPool = new ThreadPoolExecutor(1, threadsAmount,
                100L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        System.out.println("Started processing files using " + threadsAmount + " threads");
        long time = System.currentTimeMillis();
        threadPool.invokeAll(processors);
        System.out.println("Files processed in " + (System.currentTimeMillis() - time) + " milliseconds");
        threadPool.shutdown();
    }
}
