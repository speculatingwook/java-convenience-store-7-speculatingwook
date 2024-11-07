package store.domain;


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

    public boolean isLack(String name, Integer requestAmount, boolean isPromotion) {
        Item targetItem = getItem(name, isPromotion);
        return inventory.get(targetItem) < requestAmount;
    }

    public void reduceAmount(String name, Integer requestAmount, boolean isPromotion) {
        Item targetItem = getItem(name, isPromotion);
        if(!isLack(name, requestAmount, isPromotion)) {
            inventory.put(targetItem, inventory.get(targetItem) - requestAmount);
        }
        if(isPromotion && isLack(name, requestAmount, isPromotion)) {
            reduceAmountInNonPromotion(name, requestAmount - inventory.get(targetItem));
        }
    }

    public void reduceAmountInNonPromotion(String name, Integer lackAmount) throws ArithmeticException {
        Item nonPromotionItem = getItem(name, false);
        if(isLack(name, lackAmount, false)) {
            throw new ArithmeticException("[ERROR] 재고가 부족합니다.");
        }
        inventory.put(nonPromotionItem, inventory.get(nonPromotionItem) - lackAmount);
    }

    private Item getItem(String name, boolean isPromotion) {
        for (Item item : inventory.keySet()) {
            if(item.getName().equals(name) && item.getIsPromotion() == isPromotion) {
                return item;
            }
        }
        return null;
    }
}
