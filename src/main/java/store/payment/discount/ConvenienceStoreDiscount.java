package store.payment.discount;

import store.stock.item.Item;
import store.stock.item.Items;

import java.util.HashMap;

public class ConvenienceStoreDiscount implements Discount {
    private final Integer discountPercentage;
    private final Integer maxMembershipDiscountAmount;

    public ConvenienceStoreDiscount(Integer discountPercentage, Integer maxMembershipDiscountAmount) {
        this.discountPercentage = discountPercentage;
        this.maxMembershipDiscountAmount = maxMembershipDiscountAmount;
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
        int totalPrice = calculateTotalPrice(unpromotedItems);
        double discountAmount = (double) totalPrice * (discountPercentage / 100.0);
        if(discountAmount > maxMembershipDiscountAmount) {
            return (double) maxMembershipDiscountAmount;
        }
        return discountAmount;
    }

    private Integer calculateTotalPrice(Items unpromotedItems) {
        int totalPrice = 0;
        for (Item item : unpromotedItems.items().keySet()) {
            totalPrice += item.getPrice() * unpromotedItems.items().get(item);
        }
        return totalPrice;
    }
}
