package utils;

import org.apache.tika.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * A Helper Class to read files from the resource folder
 */
public class GetData {

    public static String getFileAsString(String path) {
        Path filePath = Path.of(path);
        String textFromFile;
        try (FileInputStream inputStream = new FileInputStream(String.valueOf(filePath.toAbsolutePath()))) {
            textFromFile = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to get the file at path " + filePath.toAbsolutePath());
        }
        return textFromFile;
    }

}
