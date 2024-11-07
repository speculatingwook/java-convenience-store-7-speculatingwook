package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromotionItemTest {

    @Test
    @DisplayName("정상적인 PromotionItem 생성")
    void testPromotionItemCreationSuccess() {
        // Given & When
        PromotionItem promotionItem = new PromotionItem(3, "cider", 2000, 5);

        // Then
        assertNotNull(promotionItem);
    }

    @Test
    @DisplayName("promotionCount가 0 이하일 때 예외 발생")
    void testPromotionItemCreationWithInvalidPromotionCountThrowsException() {
        // Given & When & Then
        Exception exception = assertThrows(ArithmeticException.class, () ->
                new PromotionItem(0, "cider", 2000, 5)
        );
        assertEquals("promotionCount must be greater than 0", exception.getMessage());
    }

    @Test
    @DisplayName("equals 메서드: 동일한 부모를 둔 자식 PromotionItem 객체는 같음")
    void testEqualsMethod() {
        // Given
        PromotionItem promotionItem1 = new PromotionItem(3, "cider", 2000, 5);
        PromotionItem promotionItem2 = new PromotionItem(4, "cider", 2000, 10);

        // When & Then
        assertEquals(promotionItem1, promotionItem2);
    }

    @Test
    @DisplayName("equals 메서드: 부모 객체와 자식 객체는 다름")
    void testEqualsMethodWhenTwoTypeObject() {
        // Given
        Item item = new Item("cider", 2000, 5);
        PromotionItem promotionItem = new PromotionItem(3, "cider", 2000, 5);

        // When & Then
        assertNotEquals(item, promotionItem);
    }
}
