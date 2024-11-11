package store.payment.discount;

import store.pos.OrderItemInfo;
import store.stock.Item;
import store.stock.Items;

import java.util.Map;

public interface PromotionDiscount {
    Integer receivePromotionDiscount(Items promotedItems);
    Items getOfferItems(Items promotedItems);
}
