package store.pos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.io.StoreInput;
import store.io.StoreView;
import store.stock.Inventory;
import store.stock.item.Item;
import store.ErrorCode;
import store.io.YesNoOption;
import store.stock.item.Items;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PosMachineTest {

    private Inventory inventory;
    private PosScanner scanner;
    private Cart cart;
    private PosMachine posMachine;

    @BeforeEach
    void setUp() {
        HashMap<Item, Integer> items = new HashMap<>();
        items.put(new Item("콜라", "1000"), 10);
        items.put(new Item("과자", "500"), 5);

        inventory = new Inventory(new Items(items));
        scanner = new PosScanner(inventory);
    }

    @Test
    @DisplayName("유효한 카트를 사용하여 PosMachine 초기화 시 예외가 발생하지 않음")
    void givenValidCart_whenInitializePosMachine_thenNoExceptions() {
        // given
        String request = "[콜라-1],[과자-1]";
        cart = new Cart(request);

        // when & then
        assertDoesNotThrow(() -> new PosMachine(inventory, scanner, cart));
    }

    @Test
    @DisplayName("존재하지 않는 아이템이 있는 경우 PosMachine 초기화 시 IllegalArgumentException 발생")
    void givenInvalidCartItemName_whenInitializePosMachine_thenThrowsIllegalArgumentException() {
        // given
        String request = "[사이다-1]";
        cart = new Cart(request);

        // when & then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new PosMachine(inventory, scanner, cart));
        assertEquals(ErrorCode.NO_ITEM.getErrorMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("아이템 수량이 재고를 초과하는 경우 PosMachine 초기화 시 IllegalArgumentException 발생")
    void givenExceedingItemCount_whenInitializePosMachine_thenThrowsIllegalArgumentException() {
        // given
        String request = "[콜라-15]";
        cart = new Cart(request);

        // when & then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new PosMachine(inventory, scanner, cart));
        assertEquals(ErrorCode.EXCEED_ITEM_COUNT.getErrorMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("카트 아이템을 스캔하면 주문 아이템 정보가 업데이트됨")
    void givenCartItems_whenScanCartItems_thenOrderItemInfoIsUpdated() {
        // given
        String request = "[콜라-1],[과자-1]";
        cart = new Cart(request);
        posMachine = new PosMachine(inventory, scanner, cart);

        // when
        posMachine.scanCartItems();

        // then
        assertNotNull(posMachine.getOrderItemInfo());
        assertFalse(posMachine.getOrderItemInfo().getUnpromotedItems().items().isEmpty());
    }

    @Test
    @DisplayName("YesNoOption을 사용해 주문 아이템 정보를 업데이트하면 프로모션 아이템이 올바르게 업데이트됨")
    void givenYesNoOption_whenUpdateOrderItemInfo_thenUpdatesPromotedItemsCorrectly() {
        // given
        String request = "[콜라-1],[과자-1]";
        cart = new Cart(request);
        posMachine = new PosMachine(inventory, scanner, cart);
        posMachine.scanCartItems();

        YesNoOption option = new YesNoOption(new StoreInput(new StoreView())) {
            @Override
            public boolean isMoreStockRequest(String itemName) {
                return true;
            }
        };

        // when
        posMachine.updateOrderItemInfo(option);

        // then
        assertNotNull(posMachine.getOrderItemInfo().getPromotedItems());
        assertNotNull(posMachine.getOrderItemInfo().getUnpromotedItems());
    }

    @Test
    @DisplayName("프로모션 아이템이 있는 경우 프로모션 아이템이 업데이트됨")
    void givenItemWithPromotion_whenCheckPromotedItem_thenPromotedItemIsUpdated() {
        // given
        String request = "[콜라-1],[과자-1]";
        cart = new Cart(request);
        posMachine = new PosMachine(inventory, scanner, cart);
        posMachine.scanCartItems();

        YesNoOption option = new YesNoOption(new StoreInput(new StoreView())) {
            @Override
            public boolean isMoreStockRequest(String itemName) {
                return true;
            }
        };

        // when
        posMachine.updateOrderItemInfo(option);

        // then
        assertFalse(posMachine.getOrderItemInfo().getUnpromotedItems().items().isEmpty());
    }
}
