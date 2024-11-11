package store.pos;

import store.ErrorCode;
import store.io.YesNoOption;
import store.stock.Inventory;
import store.stock.Item;

import java.util.Map;

public class PosMachine {
    private final PosScanner scanner;
    private final Cart cart;
    private final Inventory inventory;
    private OrderItemInfo orderItemInfo;

    public PosMachine(Inventory inventory, PosScanner scanner, Cart cart) {
        this.inventory = inventory;
        this.scanner = scanner;
        validate(cart);
        this.cart = cart;
    }

    private void validate(Cart cart) {
        Map<String, Integer> cartItems = cart.getCartItems();
        for (String itemName : cartItems.keySet()) {
            isItemNameCorrect(itemName);
            isItemCountValid(cartItems, itemName);
        }
    }

    private void isItemNameCorrect(String itemName) {
        if (!inventory.isItemPresent(itemName)) {
            throw new IllegalArgumentException(ErrorCode.NO_ITEM.getErrorMessage());
        }
    }

    private void isItemCountValid(Map<String, Integer> cartItems, String itemName) {
        if (cartItems.get(itemName) > inventory.getTotalAmount(itemName)) {
            throw new IllegalArgumentException(ErrorCode.EXCEED_ITEM_COUNT.getErrorMessage());
        }
    }

    public void scanCartItems() {
        ScanItemInfo scanItemInfo = scanner.scanItems(cart.getCartItems());
        this.orderItemInfo = new OrderItemInfo(scanItemInfo.getPromotedItems(), scanItemInfo.getUnpromotedItems());
    }

    public void updateOrderItemInfo(YesNoOption option) {
        Map<Item, Integer> promotedItems = orderItemInfo.getPromotedItems().items();
        for (Map.Entry<Item, Integer> entry : promotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            checkPromotedItem(item, quantity, option);
        }
    }

    public OrderItemInfo getOrderItemInfo() {
        return orderItemInfo;
    }

    private void checkPromotedItem(Item item, int quantity, YesNoOption option) {
        if (item.isPromotionEventValid()) {
            offerMorePromotedItem(item, quantity, option);
            checkPromotedItemLack(item, quantity, option);
        }
    }

    private void offerMorePromotedItem(Item item, Integer quantity, YesNoOption option) {
        if (canOfferMorePromotions(item, quantity) && option.isMoreStockRequest(item.getName())) {
            orderItemInfo.updatePromotedItem(item, quantity);
        }
    }

    private void checkPromotedItemLack(Item item, Integer quantity, YesNoOption option) {
        if (isPromotedAmountLack(item.getName(), quantity) && option.confirmPayWithNoPromotion(item.getName(), quantity)) {
            Integer totalStock = inventory.getTotalAmount(item.getName());
            Integer nonPromotionAmount = totalStock - getPromotedItemMaxSellCount(item.getName());
            orderItemInfo.updateUnPromotedItem(item, nonPromotionAmount);
        }
    }

    public boolean canOfferMorePromotions(Item item, int count) {
        if (!item.isPromotionPresent()) {
            return false;
        }

        int minimumBuyCount = item.getPromotionMinimumBuyCount();
        int offerCount = item.getPromotionOfferCount();
        return count % (minimumBuyCount + offerCount) == minimumBuyCount;
    }

    public boolean isPromotedAmountLack(String itemName, int count) {
        Item promotionItem = inventory.getItemWithPromotion(itemName);
        if (!promotionItem.isPromotionPresent()) {
            return false;
        }

        return getPromotedItemMaxSellCount(itemName) >= count;
    }

    private Integer getPromotedItemMaxSellCount(String itemName) {
        Item itemWithPromotion = inventory.getItemWithPromotion(itemName);
        int offerCount = itemWithPromotion.getPromotionOfferCount();
        int buyMinimumCount = itemWithPromotion.getPromotionMinimumBuyCount();
        int maximumSet = inventory.getItemCount(itemWithPromotion) / (offerCount + buyMinimumCount);
        return (offerCount + buyMinimumCount) * maximumSet;
    }
}
