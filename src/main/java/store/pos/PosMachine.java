package store.pos;

import store.stock.Inventory;
import store.stock.Item;
import store.io.StoreInput;

import java.util.Map;

public class PosMachine {
    private final PosScanner scanner;
    private final Cart cart;
    private final Inventory inventory;
    private final PromotionCatalog catalog;

    public PosMachine(PosScanner scanner, Cart cart, Inventory inventory, PromotionCatalog catalog) {
        this.scanner = scanner;
        this.cart = cart;
        this.inventory = inventory;
        this.catalog = catalog;
    }

    public void scanCartItems() {
        scanner.scanItems(cart.getCartItems());
    }

    public void updateCatalog(StoreInput input) {
        Map<Item, Integer> promotedItems =  catalog.getPromotedItems();
        for (Map.Entry<Item, Integer> entry : promotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            offerMorePromotedItem(item, quantity, input);
            updatePromotedCatalog(item, quantity, input);
        }
    }

    private void offerMorePromotedItem(Item item, Integer quantity, StoreInput input) {
        String yesOrNo = input.readMoreStockForDiscountRequest(item.getName());

        if(canOfferMorePromotions(item, quantity) && yesOrNo.equals("Y")) {
            cart.addItemToCart(item.getName(), item.getPromotionOfferCount());
        }
    }

    private void updatePromotedCatalog(Item item, Integer quantity, StoreInput input) {
        int unpromotedQuantity = quantity - getPromotedItemMaxSellCount(item.getName());
        String yesOrNo = input.readFullPricePaymentRequest(item.getName(), unpromotedQuantity);

        if(isPromotedAmountLack(item.getName(), quantity) && yesOrNo.equals("Y")) {
            Integer totalStock = inventory.getTotalAmount(item.getName());
            Integer nonPromotionAmount = totalStock - getPromotedItemMaxSellCount(item.getName());
            catalog.putUnPromotedItem(item, nonPromotionAmount);
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
