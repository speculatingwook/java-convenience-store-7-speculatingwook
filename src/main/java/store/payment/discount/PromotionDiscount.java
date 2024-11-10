package store.payment.discount;

import store.pos.OrderItemInfo;
import store.stock.Item;

import java.util.Map;

public interface PromotionDiscount {
    Integer receivePromotionDiscount(Map<Item, Integer> promotedItems);
}
