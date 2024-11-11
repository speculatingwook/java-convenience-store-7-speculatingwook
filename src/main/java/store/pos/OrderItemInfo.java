package store.pos;

import store.stock.item.Item;
import store.stock.item.Items;

public class OrderItemInfo {
    private final Items promotedItems;
    private final Items unpromotedItems;

    public OrderItemInfo(Items promotedItems, Items unpromotedItems) {
        this.promotedItems = promotedItems;
        this.unpromotedItems = unpromotedItems;
    }

    public Items getPromotedItems() {
        return promotedItems;
    }

    public Items getUnpromotedItems() {
        return unpromotedItems;
    }

    public Items getOrderItems() {
        return unpromotedItems.addItems(promotedItems);
    }

    public void updatePromotedItem(Item item, int count) {
        if (promotedItems.isItemExist(item)) {
            promotedItems.addAmount(item, count);
        }
        if (!promotedItems.isItemExist(item)) {
            promotedItems.addNewItem(item, count);
        }
    }

    public void updateUnPromotedItem(Item item, int count) {
        if (unpromotedItems.isItemExist(item)) {
            unpromotedItems.addAmount(item, count);
        }
        if (!unpromotedItems.isItemExist(item)) {
            unpromotedItems.addNewItem(item, count);
        }
    }
}
