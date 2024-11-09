package store.pos;

import store.stock.Inventory;
import store.stock.Item;

import java.util.Map;

/**
 * 제품을 스캔하고, 제품에 대한 정보를 처리하여 알려주는 역할
 */
public class PosScanner {
    private final Inventory inventory;
    private final PromotionCatalog catalog;

    public PosScanner(Inventory inventory, PromotionCatalog catalog) {
        this.inventory = inventory;
        this.catalog = catalog;
    }

    public void scanItems(Map<String, Integer> items) {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemName = entry.getKey();
            Integer itemCount = entry.getValue();
            classifyUnpromotedItems(itemName, itemCount);
            classifyPromotedItems(itemName, itemCount);
        }
    }

    private void classifyUnpromotedItems(String itemName, Integer itemCount) {
        if(inventory.isItemInPromotion(itemName)) {
            Item unpromotedItem = inventory.getItemWithPromotion(itemName);
            catalog.putUnPromotedItem(unpromotedItem, itemCount);
        }
    }

    private void classifyPromotedItems(String itemName, Integer itemCount) {
        if(!inventory.isItemInPromotion(itemName)) {
            Item promotedItem = inventory.getItemWithPromotion(itemName);
            catalog.putPromotedItem(promotedItem, itemCount);
        }
    }
}
