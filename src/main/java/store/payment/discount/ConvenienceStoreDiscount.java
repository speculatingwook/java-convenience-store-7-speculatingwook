package store.payment.discount;

import store.stock.Inventory;
import store.stock.Item;

import java.util.Map;

public class ConvenienceStoreDiscount implements Discount {
    private final Integer discountPercentage;
    private final Inventory inventory;

    public ConvenienceStoreDiscount(Integer discountPercentage, Inventory inventory) {
        this.discountPercentage = discountPercentage;
        this.inventory = inventory;
    }

    @Override
    public Integer receivePromotionDiscount(Map<Item, Integer> promotedItems) {
        int totalDiscount = 0;
        for (Map.Entry<Item, Integer> entry : promotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            totalDiscount += item.getPrice() * quantity;
        }
        return totalDiscount;
    }

    @Override
    public Double receiveMembershipDiscount(Map<Item, Integer> unpromotedItems) {
        int totalPrice = 0;
        for (Map.Entry<Item, Integer> entry : unpromotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;
        }
        return (double) (totalPrice / discountPercentage);
    }
}
