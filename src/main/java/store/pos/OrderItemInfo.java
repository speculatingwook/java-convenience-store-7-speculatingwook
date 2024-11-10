package store.pos;

import store.stock.Item;

import java.util.HashMap;
import java.util.Map;

public class OrderItemInfo {
    private final Map<Item, Integer> promotedItems;
    private final Map<Item, Integer> unpromotedItems;

    public OrderItemInfo(ScanItemInfo scanItemInfo) {
        this.promotedItems = scanItemInfo.getPromotedItems();
        this.unpromotedItems = scanItemInfo.getUnpromotedItems();
    }

    public Map<Item, Integer> getPromotedItems() {
        return promotedItems;
    }

    public Map<Item, Integer> getUnpromotedItems() {
        return unpromotedItems;
    }

    public void updatePromotedItem(Item item, int count) {
        if(promotedItems.containsKey(item)) {
            promotedItems.put(item, promotedItems.get(item) + count);
        }
        if(!promotedItems.containsKey(item)) {
            promotedItems.put(item, count);
        }
    }

    public void updateUnPromotedItem(Item item, int count) {
        if(unpromotedItems.containsKey(item)) {
            unpromotedItems.put(item, unpromotedItems.get(item) + count);
        }
        if(!unpromotedItems.containsKey(item)) {
            unpromotedItems.put(item, count);
        }
    }
}