package store.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.ErrorCode;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private Inventory inventory;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        Promotion promotion = new Promotion("탄산2+1", "2", "1", "2024-01-31", "2025-10-10");
        item1 = new Item("음료수", "1000", promotion);

        item2 = new Item("과자", "1500");

        HashMap<Item, Integer> initialInventory = new HashMap<>();
        initialInventory.put(item1, 10);
        initialInventory.put(item2, 5);

        inventory = new Inventory(initialInventory);
    }

    @Test
    @DisplayName("초기 재고 상태가 유효하게 설정되는지 확인")
    void testInventoryCreation_validInitialInventory() {
        // given, when
        Map<Item, Integer> currentInventory = inventory.getInventory();

        // then
        assertEquals(10, currentInventory.get(item1));
        assertEquals(5, currentInventory.get(item2));
    }

    @Test
    @DisplayName("재고 수가 음수인 경우 예외가 발생한다")
    void testInventoryCreation_negativeStock() {
        // given
        HashMap<Item, Integer> invalidInventory = new HashMap<>();
        invalidInventory.put(item1, -5);

        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Inventory(invalidInventory));
        assertEquals(ErrorCode.COUNT_UNDER_ZERO.getCriticalMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("재고에서 아이템이 올바르게 차감된다")
    void testDeductItems() {
        // given
        Map<Item, Integer> itemsToDeduct = Map.of(item1, 3, item2, 2);

        // when
        inventory.deductItems(itemsToDeduct);

        // then
        assertEquals(7, inventory.getItemCount(item1));
        assertEquals(3, inventory.getItemCount(item2));
    }

    @Test
    @DisplayName("아이템이 있는지 여부를 확인한다")
    void testIsItemPresent() {
        // given, when
        boolean result1 = inventory.isItemPresent("음료수");
        boolean result2 = inventory.isItemPresent("사탕");

        // then
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    @DisplayName("프로모션이 적용된 아이템인지 확인한다")
    void testIsItemInPromotion() {
        // given, when
        boolean result1 = inventory.isItemInPromotion("음료수");
        boolean result2 = inventory.isItemInPromotion("과자");

        // then
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    @DisplayName("아이템의 총 재고 수량을 확인한다")
    void testGetTotalAmount() {
        // given, when
        int totalAmount1 = inventory.getTotalAmount("음료수");
        int totalAmount2 = inventory.getTotalAmount("과자");

        // then
        assertEquals(10, totalAmount1);
        assertEquals(5, totalAmount2);
    }

    @Test
    @DisplayName("프로모션이 적용된 아이템을 반환한다")
    void testGetItemWithPromotion() {
        // given, when
        Item promoItem = inventory.getItemWithPromotion("음료수");
        Item nonPromoItem = inventory.getItemWithPromotion("과자");

        // then
        assertEquals(item1, promoItem);
        assertNull(nonPromoItem);
    }

    @Test
    @DisplayName("프로모션이 적용되지 않은 아이템을 반환한다")
    void testGetItemWithoutPromotion() {
        // given, when
        Item nonPromoItem = inventory.getItemWithoutPromotion("과자");
        Item promoItem = inventory.getItemWithoutPromotion("음료수");

        // then
        assertEquals(item2, nonPromoItem);
        assertNull(promoItem);
    }

    @Test
    @DisplayName("프로모션이 종료된 아이템을 재고에서 제거하고 기본 상태로 전환한다")
    void testRefresh() {
        // given
        item2 = new Item("음료수", "1000");
        inventory.getInventory().put(item1, 1);
        inventory.getInventory().put(item2, 5);

        // when
        inventory.refresh();

        // then
        assertEquals(0, inventory.getItemCount(item1));
        assertEquals(6, inventory.getItemCount(item2));
    }

    @Test
    @DisplayName("재고 정보를 문자열로 반환한다")
    void testToString() {
        // given, when
        String stockString = inventory.toString();

        // then
        assertTrue(stockString.contains("음료수 1,000원 10개 탄산2+1"));
        assertTrue(stockString.contains("과자 1,500원 5개"));
    }
}
