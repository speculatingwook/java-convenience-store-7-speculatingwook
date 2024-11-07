package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        HashMap<Item, Integer> initialInventory = new HashMap<>();
        initialInventory.put(new Item("cola", 1500), 10);
        initialInventory.put(new Item("cola", 1000, 2), 5); // 프로모션 상품
        initialInventory.put(new Item("cider", 2000), 2);
        inventory = new Inventory(initialInventory);
    }

    @Test
    @DisplayName("isLack 메서드: 재고가 충분하지 않으면 true를 반환")
    void testIsLackInsufficientStock() {
        // Given
        String name = "cola";
        Integer requestAmount = 15;
        boolean isPromotion = false;

        // When
        boolean result = inventory.isLack(name, requestAmount, isPromotion);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("isLack 메서드: 재고가 충분하면 false를 반환")
    void testIsLackSufficientStock() {
        // Given
        String name = "cola";
        Integer requestAmount = 5;
        boolean isPromotion = true;

        // When
        boolean result = inventory.isLack(name, requestAmount, isPromotion);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("reduceAmount 메서드: 요청된 양만큼 재고가 감소")
    void testReduceAmount() {
        // Given
        String name = "cola";
        Integer requestAmount = 3;
        boolean isPromotion = false;

        // When
        inventory.reduceAmount(name, requestAmount, isPromotion);

        // Then
        Item item = inventory.getItem(name, isPromotion);
        assertEquals(7, inventory.getInventoryAmount(item));
    }

    @Test
    @DisplayName("reduceAmount 메서드: 프로모션 재고가 부족하면 비프로모션 재고로 대체")
    void testReduceAmountFallbackToNonPromotion() {
        // Given
        String name = "cola";
        Integer requestAmount = 7; // 프로모션 재고 5개, 나머지는 비프로모션에서 차감

        // When
        inventory.reduceAmount(name, requestAmount, true);

        // Then
        Item promotionItem = inventory.getItem(name, true);
        Item nonPromotionItem = inventory.getItem(name, false);
        assertEquals(0, inventory.getInventoryAmount(promotionItem)); // 프로모션 재고 소진
        assertEquals(8, inventory.getInventoryAmount(nonPromotionItem)); // 비프로모션 재고 10 - 2 = 8
    }

    @Test
    @DisplayName("reduceAmountInNonPromotion 메서드: 프로모션이 아닌 재고가 부족하면 예외 발생")
    void testReduceAmountInNonPromotionThrowsExceptionWhenInsufficientStock() {
        // Given
        String name = "cider";
        Integer requestAmount = 10;

        // Expect
        assertThrows(ArithmeticException.class, () -> inventory.reduceAmount(name, requestAmount, false));
    }
}
