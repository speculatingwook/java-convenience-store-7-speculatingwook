package store.stock;


import store.util.parser.ConvenienceStoreParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private final HashMap<Item, Integer> inventory;

    public Inventory(HashMap<Item, Integer> inventory) {
        validate(inventory);
        this.inventory = inventory;
    }

    private void validate(HashMap<Item, Integer> inventory) {
        for (Item item : inventory.keySet()) {
            if (inventory.get(item) < 0) {
                throw new ArithmeticException("[CRITICAL] the amount cannot be negative.");
            }
        }
    }

    public void deductItems(Map<Item, Integer> items) {
        for (Item item : items.keySet()) {
            Integer amount = items.get(item);
            deductItem(item, amount);
        }
    }

    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    public Integer getItemCount(Item item) {
        return inventory.get(item);
    }

    public boolean isItemPresent(String itemName) {
        return inventory.keySet().stream()
                .anyMatch(item -> item.getName().equals(itemName));
    }

    public boolean isItemInPromotion(String itemName) {
        Item itemWithPromotion = getItemWithPromotion(itemName);
        return !(itemWithPromotion == null);
    }

    /**
     *
     */
    public void refresh() {
        for (Item item : inventory.keySet()) {
            Integer amount = inventory.get(item);
            if (item.isPromotionPresent() && isItemOutOfPromotion(item, amount)) {
                inventory.put(item, inventory.get(item) - amount);
                Item unpromotedItem = getItemWithoutPromotion(item.getName());
                inventory.put(unpromotedItem, inventory.get(unpromotedItem) + amount);
            }
        }
    }

    public Integer getTotalAmount(String itemName) {
        List<Item> totalItems = inventory.keySet().stream().filter(item -> item.getName().equals(itemName)).toList();
        int totalAmount = 0;
        for (Item item : totalItems) {
            Integer amount = inventory.get(item);
            totalAmount += amount;
        }
        return totalAmount;
    }

    public Item getItemWithPromotion(String itemName) {
        for (Item item : inventory.keySet()) {
            if (item.getName().equals(itemName) && item.isPromotionPresent()) {
                return item;
            }
        }
        return null;
    }

    public Item getItemWithoutPromotion(String itemName) {
        for (Item item : inventory.keySet()) {
            if (item.getName().equals(itemName) && !item.isPromotionPresent()) {
                return item;
            }
        }
        return null;
    }

    private void deductItem(Item item, int amount) {
        inventory.put(item, inventory.get(item) - amount);
    }

    private boolean isItemOutOfPromotion(Item item, int count) {
        return count <= item.getPromotionMinimumBuyCount();
    }

    @Override
    public String toString() {
        StringBuilder currentStock = new StringBuilder();
        for (Item item : inventory.keySet()) {
            currentStock.append(addDefaultInfo(item))
                    .append(addStockCountInfo(item))
                    .append(addPromotionInfo(item));
        }
        return currentStock.toString();
    }

    private StringBuilder addPromotionInfo(Item item) {
        StringBuilder promotionInfo = new StringBuilder();
        promotionInfo.append(" ")
                .append(item.getPromotionName())
                .append("\n");
        return promotionInfo;
    }

    private StringBuilder addStockCountInfo(Item item) {
        StringBuilder stockCount = new StringBuilder();
        Integer count = inventory.get(item);
        if (!count.equals(0)) {
            stockCount.append(inventory.get(item)).append("개");
        }
        if (count.equals(0)) {
            stockCount.append("재고 없음");
        }
        return stockCount;
    }

    private StringBuilder addDefaultInfo(Item item) {
        StringBuilder defaultInfo = new StringBuilder();
        defaultInfo.append("- ")
                .append(item.getName())
                .append(" ")
                .append(ConvenienceStoreParser.formatNumberWithComma(item.getPrice()))
                .append("원 ");
        return defaultInfo;
    }
}
