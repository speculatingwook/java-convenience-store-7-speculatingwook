package store.io.reader;

import java.io.*;
import java.util.stream.Collectors;

public class FileReader implements Reader {

    @Override
    public String read(String fileName) {
        InputStream inputStream = getInputStream(fileName);
        if (inputStream == null) {
            return null;
        }
        return readFile(inputStream);
    }

    private InputStream getInputStream(String fileName) {
        InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            System.out.println("File not found: " + fileName);
        }
        return inputStream;
    }

    private String readFile(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

}