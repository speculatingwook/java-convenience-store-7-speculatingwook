package store.payment.discount;

import store.stock.Item;

import java.util.Map;

public interface MembershipDiscount {
    Double receiveMembershipDiscount(Map<Item, Integer> unpromotedItems);
}
