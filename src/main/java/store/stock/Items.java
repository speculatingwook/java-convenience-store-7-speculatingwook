package store.stock;

import java.util.HashMap;

public record Items(HashMap<Item, Integer> items) {
    public Items addItems(Items items) {
        this.items.putAll(items.items);
        return this;
    }

    public boolean isItemExist(Item item) {
        return items.containsKey(item);
    }

    public void addAmount(Item item, int amount) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + amount);
        }
    }

    public void addNewItem(Item item, int amount) {
        if (!items.containsKey(item)) {
            items.put(item, amount);
        }
    }
}
