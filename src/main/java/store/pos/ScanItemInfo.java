package store.pos;

import store.stock.Item;

import java.util.HashMap;
import java.util.Map;

public class ScanItemInfo {
    private final Map<Item, Integer> promotedItems;
    private final Map<Item, Integer> unpromotedItems;

    public ScanItemInfo() {
        this.promotedItems = new HashMap<>();
        this.unpromotedItems = new HashMap<>();
    }

    public Map<Item, Integer> getPromotedItems() {
        return promotedItems;
    }

    public Map<Item, Integer> getUnpromotedItems() {
        return unpromotedItems;
    }

    public Map<Item, Integer> getOrderItems() {
        Map<Item, Integer> orderItems = new HashMap<>();
        orderItems.putAll(promotedItems);
        orderItems.putAll(unpromotedItems);
        return orderItems;
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
