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

    public boolean isItemPurchasable(String item, int requestAmount){
        return getTotalAmount(item) >= requestAmount;
    }

    public Integer getTotalAmount(String item){
        Item itemWithPromotion = getItemWithPromotion(item);
        Item itemWithoutPromotion = getItemWithoutPromotion(item);
        return inventory.get(itemWithPromotion) + inventory.get(itemWithoutPromotion);
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



    public Integer getInventoryAmount(Item item) {
        return inventory.get(item);
    }
}
