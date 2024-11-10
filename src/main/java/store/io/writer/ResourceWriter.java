package store.io.writer;

import java.io.*;
import java.nio.file.Paths;
import java.util.Objects;

public class ResourceWriter implements Writer {

    @Override
    public boolean write(String fileName, String content) {
        File file = createFile(fileName);
        if (file == null) {
            return false;
        }
        return writeToFile(file, content);
    }

    private File createFile(String fileName) {
        String path = Paths.get("").toAbsolutePath().toString();
        File file = new File(path + "/src/main/resources/" + fileName);
        if (!file.exists()) {
            System.out.println("File does not exist: " + fileName);
            return null;
        }
        return file;
    }

    private boolean writeToFile(File file, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
        }
        return true;
    }

}
