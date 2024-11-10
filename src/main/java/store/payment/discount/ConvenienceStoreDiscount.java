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
        for (Item item : promotedItems.keySet()) {
            int setWithPromotion = promotedItems.get(item) / (item.getPromotionOfferCount() + item.getPromotionOfferCount());
            totalDiscount += item.getPrice() * setWithPromotion;
        }
        return totalDiscount;
    }

    @Override
    public Double receiveMembershipDiscount(Map<Item, Integer> unpromotedItems) {
        int totalPrice = 0;
        for (Item item : unpromotedItems.keySet()) {
            totalPrice += item.getPrice() * unpromotedItems.get(item);
        }
        return (double) (totalPrice / discountPercentage);
    }
}
