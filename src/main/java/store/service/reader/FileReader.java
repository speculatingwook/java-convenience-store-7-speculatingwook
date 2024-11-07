package store.service.reader;

import java.io.*;

public class FileReader implements Reader {

    @Override
    public String read(String fileName) {
        StringBuilder content = new StringBuilder();

        try (InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(fileName)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

            }
        } catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return content.toString();
    }

}