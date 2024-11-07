package store.service.discount;

import store.domain.Inventory;
import store.domain.Item;

public class ConvenienceStoreDiscount implements Discount {
    private final Integer discountPercentage;
    private final Inventory inventory;

    public ConvenienceStoreDiscount(Integer discountPercentage, Inventory inventory) {
        this.discountPercentage = discountPercentage;
        this.inventory = inventory;
    }

    @Override
    public Integer calculatePromotionItemCount(String itemName, int requestCount) {
        Item itemWithPromotion = inventory.getItemWithPromotion(itemName);
        int offerCount = itemWithPromotion.getPromotionOfferCount();
        int buyMinimumCount = itemWithPromotion.getPromotionMinimumBuyCount();
        int maximumSet = inventory.getItemCount(itemWithPromotion) / (offerCount + buyMinimumCount);

        return maximumSet * buyMinimumCount;
    }

    @Override
    public Double calculateMembershipDiscount(int amount) {
        return (double) (amount / discountPercentage);
    }
}
