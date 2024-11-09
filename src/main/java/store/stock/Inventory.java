package store.stock;


import store.parser.ConvenienceStoreParser;

import java.util.HashMap;

public class Inventory {
    private final HashMap<Item, Integer> inventory;

    public Inventory(HashMap<Item, Integer> inventory) {
        validate(inventory);
        this.inventory = inventory;
    }

    private void validate(HashMap<Item, Integer> inventory) {
        for(Item item : inventory.keySet()) {
            if (inventory.get(item) < 0) {
                throw new ArithmeticException("[CRITICAL] the amount cannot be negative.");
            }
        }
    }

    public boolean isItemPurchasable(String item, int requestAmount){
        return getTotalAmount(item) >= requestAmount;
    }

    public void deductItem(String itemName, int amount) {
        Item itemWithPromotion = getItemWithPromotion(itemName);
        Item itemWithoutPromotion = getItemWithoutPromotion(itemName);

        executeWhenPromotionInSufficient(itemWithPromotion,itemWithoutPromotion,amount);
        executeWhenPromotionSufficient(itemWithPromotion, amount);
    }

    public Integer getItemCount(Item item) {
        return inventory.get(item);
    }

    public Integer getTotalItemAmount(String itemName) {
        Item itemWithPromotion = getItemWithPromotion(itemName);
        Item itemWithoutPromotion = getItemWithoutPromotion(itemName);
        return inventory.get(itemWithPromotion) + inventory.get(itemWithoutPromotion);
    }

    public boolean isItemInPromotion(String itemName) {
        Item itemWithPromotion = getItemWithPromotion(itemName);
        return !(itemWithPromotion == null);
    }


    private void executeWhenPromotionInSufficient(Item itemWithPromotion, Item itemWithoutPromotion, int amount) {
        if(itemWithPromotion != null && inventory.get(itemWithPromotion) < amount) {
            deductItemWhenAmountsCombined(itemWithPromotion,itemWithoutPromotion,amount);
        }
    }

    private void executeWhenPromotionSufficient(Item itemWithPromotion, int amount) {
        if (itemWithPromotion != null && inventory.get(itemWithPromotion) > amount) {
            deductItem(itemWithPromotion, amount);
        }
    }

    private void deductItemWhenAmountsCombined(Item itemWithPromotion, Item itemWithoutPromotion, int amount) {
        int leftAmount = inventory.get(itemWithPromotion) - amount;
        deductItem(itemWithPromotion, amount);
        deductItem(itemWithoutPromotion, leftAmount);
    }

    private void deductItem(Item item, int amount){
        inventory.put(item, inventory.get(item) - amount);
    }

    public Integer getTotalAmount(String item){
        Item itemWithPromotion = getItemWithPromotion(item);
        Item itemWithoutPromotion = getItemWithoutPromotion(item);
        return inventory.get(itemWithPromotion) + inventory.get(itemWithoutPromotion);
    }

    public Item getItem(String itemName) {
        return inventory.keySet().stream().filter(item -> item.getName().equals(itemName)).findFirst().orElse(null);
    }

    public Item getItemWithPromotion(String itemName){
        for (Item item : inventory.keySet()) {
            if(item.getName().equals(itemName) && item.isPromotionPresent()){
                return item;
            }
        }
        return null;
    }

    public Item getItemWithoutPromotion(String itemName){
        for (Item item : inventory.keySet()) {
            if(item.getName().equals(itemName) && !item.isPromotionPresent()){
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder currentStock = new StringBuilder();
        for(Item item : inventory.keySet()) {
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
        if (count.equals(0)){
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
