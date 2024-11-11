package store.payment.discount;

import store.pos.OrderItemInfo;
import store.stock.Item;
import store.stock.Items;

import java.util.Map;

public interface Discount extends MembershipDiscount, PromotionDiscount {
    Integer receivePromotionDiscount(Items promotedItems);
    Items getOfferItems(Items promotedItems);

    Double receiveMembershipDiscount(Items unpromotedItems);
}
