package store.payment.discount;

import store.stock.item.Items;

public interface Discount extends MembershipDiscount, PromotionDiscount {
    Integer receivePromotionDiscount(Items promotedItems);
    Items getOfferItems(Items promotedItems);

    Double receiveMembershipDiscount(Items unpromotedItems);
}
