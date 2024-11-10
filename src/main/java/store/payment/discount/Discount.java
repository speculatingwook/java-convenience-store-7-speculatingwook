package store.payment.discount;

import store.pos.OrderItemInfo;
import store.stock.Item;

import java.util.Map;

public interface Discount extends MembershipDiscount, PromotionDiscount {
    Integer receivePromotionDiscount(Map<Item, Integer> promotedItems);

    Double receiveMembershipDiscount(Map<Item, Integer> unpromotedItems);
}
