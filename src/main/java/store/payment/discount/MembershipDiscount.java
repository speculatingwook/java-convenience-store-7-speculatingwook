package store.payment.discount;

import store.stock.item.Items;

public interface MembershipDiscount {
    Double receiveMembershipDiscount(Items unpromotedItems);
}
