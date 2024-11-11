package store.payment.discount;

import store.stock.Inventory;
import store.stock.Item;
import store.stock.Items;

import java.util.HashMap;

public class ConvenienceStoreDiscount implements Discount {
    private final Integer discountPercentage;

    public ConvenienceStoreDiscount(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Items getOfferItems(Items promotedItems) {
        HashMap<Item, Integer> offerItems = new HashMap<>();
        for (Item item : promotedItems.items().keySet()) {
            int setWithPromotion = promotedItems.items().get(item) / (item.getPromotionMinimumBuyCount() + item.getPromotionOfferCount());
            offerItems.put(item, setWithPromotion);
        }
        return new Items(offerItems);
    }

    @Override
    public Integer receivePromotionDiscount(Items promotedItems) {
        int totalDiscount = 0;
        for (Item item : promotedItems.items().keySet()) {
            int setWithPromotion = promotedItems.items().get(item) / (item.getPromotionMinimumBuyCount() + item.getPromotionOfferCount());
            totalDiscount += item.getPrice() * setWithPromotion;
        }
        return totalDiscount;
    }

    @Override
    public Double receiveMembershipDiscount(Items unpromotedItems) {
        int totalPrice = 0;
        for (Item item : unpromotedItems.items().keySet()) {
            totalPrice += item.getPrice() * unpromotedItems.items().get(item);
        }
        return (double) (totalPrice / discountPercentage);
    }
}
