package store.service;

import org.junit.jupiter.api.Test;
import store.io.reader.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileReaderTest {

    @Test
    public void testReadFile() {
        FileReader fileReader = new FileReader();
        String content = fileReader.read("products.md");
        String expectedOutput = "name,price,quantity,promotion\n" +
                "콜라,1000,10,탄산2+1\n" +
                "콜라,1000,10,null\n" +
                "사이다,1000,8,탄산2+1\n" +
                "사이다,1000,7,null\n" +
                "오렌지주스,1800,9,MD추천상품\n" +
                "탄산수,1200,5,탄산2+1\n" +
                "물,500,10,null\n" +
                "비타민워터,1500,6,null\n" +
                "감자칩,1500,5,반짝할인\n" +
                "감자칩,1500,5,null\n" +
                "초코바,1200,5,MD추천상품\n" +
                "초코바,1200,5,null\n" +
                "에너지바,2000,5,null\n" +
                "정식도시락,6400,8,null\n" +
                "컵라면,1700,1,MD추천상품\n" +
                "컵라면,1700,10,null\n";
        
        assertEquals(expectedOutput, content);
    }
}
