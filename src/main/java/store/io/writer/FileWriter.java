package store.io.writer;

import java.io.*;

public class FileWriter implements Writer {

    @Override
    public boolean write(String fileName, String content) {
        try (OutputStream outputStream = new FileOutputStream(fileName);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

            writer.write(content);
            writer.flush();

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
        }

        return true;
    }
}
