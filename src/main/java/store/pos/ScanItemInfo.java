package store.pos;

import store.stock.item.Item;
import store.stock.item.Items;

import java.util.HashMap;

public class ScanItemInfo {
    private final Items promotedItems;
    private final Items unpromotedItems;

    public ScanItemInfo() {
        this.promotedItems = new Items(new HashMap<>());
        this.unpromotedItems = new Items(new HashMap<>());
    }

    public Items getPromotedItems() {
        return promotedItems;
    }

    public Items getUnpromotedItems() {
        return unpromotedItems;
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
