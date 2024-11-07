package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    @DisplayName("아이템을 성공적으로 만들 수 있다.")
    void testItemCreationSuccess() {
        // Given & When
        Item appleItem = new Item("Apple", 1000, 10);

        // Then
        assertNotNull(appleItem);
    }

    @Test
    @DisplayName("아이템에 음수 가격이 들어가면 생성될 수 없다.")
    void testItemCreationWithNegativePriceThrowsException() {
        // Given & When & Then
        Exception exception = assertThrows(ArithmeticException.class, () ->
                new Item("Apple", -1000, 10)
        );
        assertEquals("[ERROR] 금액은 음수가 불가능합니다.", exception.getMessage());
    }

}
