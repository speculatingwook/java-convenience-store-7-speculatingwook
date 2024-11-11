package store.stock.item;

import java.util.HashMap;

public record Items(HashMap<Item, Integer> items) {
    public Items addItems(Items items) {
        Items newItems = new Items(new HashMap<>(this.items));
        newItems.items.putAll(items.items);
        return newItems;
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

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
