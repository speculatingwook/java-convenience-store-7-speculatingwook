package store.payment.discount;

import store.stock.Item;
import store.stock.Items;

import java.util.Map;

public interface MembershipDiscount {
    Double receiveMembershipDiscount(Items unpromotedItems);
}
