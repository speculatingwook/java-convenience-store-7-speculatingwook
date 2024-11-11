package store.payment.discount;

import store.stock.item.Items;

public interface PromotionDiscount {
    Integer receivePromotionDiscount(Items promotedItems);
    Items getOfferItems(Items promotedItems);
}
