package store.pos;

import store.stock.Inventory;
import store.stock.Item;
import store.stock.Promotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PosScannerTest {

    private Inventory inventory;
    private PosScanner posScanner;
    private Item itemWithPromotion;
    private Item itemWithoutPromotion;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotion = new Promotion("1+1", "1", "1", "2024-11-01", "2024-11-30");

        itemWithPromotion = new Item("콜라", "1000", promotion);
        itemWithoutPromotion = new Item("사이다", "500");
        HashMap<Item, Integer> items = new HashMap<>();
        items.put(itemWithPromotion, 3);
        items.put(itemWithoutPromotion, 5);

        inventory = new Inventory(items);

        posScanner = new PosScanner(inventory);
    }

    @Test
    @DisplayName("프로모션이 적용된 아이템을 스캔할 때 제대로 분류한다.")
    void testScanItems_withPromotedItem() {
        // given
        Map<String, Integer> itemsToScan = new HashMap<>();
        itemsToScan.put("콜라", 2);

        // when
        ScanItemInfo scanItemInfo = posScanner.scanItems(itemsToScan);

        // then
        assertTrue(scanItemInfo.getPromotedItems().containsKey(itemWithPromotion));
        assertEquals(2, scanItemInfo.getPromotedItems().get(itemWithPromotion));
    }

    @Test
    @DisplayName("프로모션이 적용되지 않은 일반 아이템을 스캔할 때 제대로 분류한다.")
    void testScanItems_withRegularItem() {
        // given
        Map<String, Integer> itemsToScan = new HashMap<>();
        itemsToScan.put("사이다", 3);

        // when
        ScanItemInfo scanItemInfo = posScanner.scanItems(itemsToScan);

        // then
        assertTrue(scanItemInfo.getUnpromotedItems().containsKey(itemWithoutPromotion));
        assertEquals(3, scanItemInfo.getUnpromotedItems().get(itemWithoutPromotion));
    }

    @Test
    @DisplayName("프로모션이 적용된 아이템과 일반 아이템을 동시에 스캔할 때 제대로 분류한다.")
    void testScanItems_withBothPromotedAndRegularItems() {
        // given
        Map<String, Integer> itemsToScan = new HashMap<>();
        itemsToScan.put("콜라", 2);
        itemsToScan.put("사이다", 3);

        // when
        ScanItemInfo scanItemInfo = posScanner.scanItems(itemsToScan);

        // then
        assertTrue(scanItemInfo.getPromotedItems().containsKey(itemWithPromotion));
        assertEquals(2, scanItemInfo.getPromotedItems().get(itemWithPromotion));

        assertTrue(scanItemInfo.getUnpromotedItems().containsKey(itemWithoutPromotion));
        assertEquals(3, scanItemInfo.getUnpromotedItems().get(itemWithoutPromotion));
    }

    @Test
    @DisplayName("프로모션 기간이 지난 아이템을 스캔할 때 일반 상품으로 분류한다.")
    void testScanItems_whenItemIsOutOfPromotion() {
        // given
        Promotion expiredPromotion = new Promotion("Expired Promotion", "1", "1", "2023-10-01", "2023-10-30");
        Item expiredItem = new Item("콜라", "1000", expiredPromotion);

        HashMap<Item, Integer> items = new HashMap<>();
        items.put(expiredItem, 3);
        inventory = new Inventory(items);
        posScanner = new PosScanner(inventory);

        // when
        Map<String, Integer> itemsToScan = new HashMap<>();
        itemsToScan.put("콜라", 1);

        ScanItemInfo scanItemInfo = posScanner.scanItems(itemsToScan);

        // then
        assertTrue(scanItemInfo.getUnpromotedItems().containsKey(expiredItem));
        assertEquals(1, scanItemInfo.getUnpromotedItems().get(expiredItem));
    }
}
