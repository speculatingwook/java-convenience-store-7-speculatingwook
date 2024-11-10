package store.pos;

import store.stock.Inventory;
import store.stock.Item;
import store.io.StoreInput;

import java.util.Map;

public class PosMachine {
    private final PosScanner scanner;
    private final Cart cart;
    private final Inventory inventory;
    private OrderItemInfo orderItemInfo;

    public PosMachine(PosScanner scanner, Cart cart, Inventory inventory) {
        this.scanner = scanner;
        this.cart = cart;
        this.inventory = inventory;
    }

    public void scanCartItems() {
        ScanItemInfo scanItemInfo = scanner.scanItems(cart.getCartItems());
        this.orderItemInfo = new OrderItemInfo(scanItemInfo.getPromotedItems(), scanItemInfo.getUnpromotedItems());
    }

    public OrderItemInfo updateOrderItemInfo(StoreInput input) {
        Map<Item, Integer> promotedItems =  orderItemInfo.getPromotedItems();
        for (Map.Entry<Item, Integer> entry : promotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            checkPromotedItem(item, quantity, input);
        }
        return orderItemInfo;
    }

    public OrderItemInfo getOrderItemInfo() {
        return orderItemInfo;
    }

    private void checkPromotedItem(Item item, int quantity, StoreInput input) {
        if(item.isPromotionEventValid()){
            offerMorePromotedItem(item, quantity, input);
            checkPromotedItemLack(item, quantity, input);
        }
    }

    private void offerMorePromotedItem(Item item, Integer quantity, StoreInput input) {
        if(canOfferMorePromotions(item, quantity)) {
            String yesOrNo = input.readMoreStockForDiscountRequest(item.getName());

            if(yesOrNo.equals("Y")) {
                orderItemInfo.updatePromotedItem(item, quantity);
            }
        }
    }

    private void checkPromotedItemLack(Item item, Integer quantity, StoreInput input) {
        if(isPromotedAmountLack(item.getName(), quantity)){
            int unpromotedQuantity = quantity - getPromotedItemMaxSellCount(item.getName());
            String yesOrNo = input.readFullPricePaymentRequest(item.getName(), unpromotedQuantity);
            updatePromotedItemToUnpromotedItem(yesOrNo, item);
        }
    }

    private void updatePromotedItemToUnpromotedItem(String yesOrNo, Item item) {
        if(yesOrNo.equals("Y")) {
            Integer totalStock = inventory.getTotalAmount(item.getName());
            Integer nonPromotionAmount = totalStock - getPromotedItemMaxSellCount(item.getName());
            orderItemInfo.updateUnPromotedItem(item, nonPromotionAmount);
        }
    }

    public boolean canOfferMorePromotions(Item item, int count) {
        if(!item.isPromotionPresent()) {
            return false;
        }

        int minimumBuyCount = item.getPromotionMinimumBuyCount();
        int offerCount = item.getPromotionOfferCount();
        return count % (minimumBuyCount + offerCount) == minimumBuyCount;
    }

    public boolean isPromotedAmountLack(String itemName, int count) {
        Item promotionItem = inventory.getItemWithPromotion(itemName);
        if(!promotionItem.isPromotionPresent()) {
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
