package org.qubership.landscape.processor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<String> findAllFilesRecursively(String rootDirName, String ... allowedExt) {
        List<String> result = new ArrayList<>();

        _findAllFilesRecursively(result, new File(rootDirName), allowedExt);

        return result;
    }

    private static void _findAllFilesRecursively(List<String> collectedFiles, File dirToScan, String ... allowedExt) {
        TheLogger.debug("Searching additional configuration items at '"+ dirToScan + "'");
        File[] items = dirToScan.listFiles();
        if (items == null) return;

        for (File item : items) {
            TheLogger.debug("    Processing item '" + item + "'");
            if (item.isDirectory() && item.exists()) {
                _findAllFilesRecursively(collectedFiles, item, allowedExt);
            } else {
                String fileName = item.toString();
                for (String ext : allowedExt) {
                    if (fileName.endsWith(ext)) {
                        collectedFiles.add(fileName);
                    }
                }
            }
        }
    }

    public static void saveToFile(String content, String fileToWriteInto) throws IOException {
        Path path = Paths.get(fileToWriteInto);
        Files.write(path, content.getBytes());
    }

    public static String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
