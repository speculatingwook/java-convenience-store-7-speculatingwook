package store.pos;

import store.stock.Inventory;
import store.stock.Item;

import java.util.Map;

/**
 * 제품을 스캔하고, 제품에 대한 정보를 처리하여 알려주는 역할
 */
public class PosScanner {
    private final Inventory inventory;
    private final ScanItemInfo scanItemInfo;

    public PosScanner(Inventory inventory, ScanItemInfo scanItemInfo) {
        this.inventory = inventory;
        this.scanItemInfo = scanItemInfo;
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
            scanItemInfo.updateUnPromotedItem(unpromotedItem, itemCount);
        }
    }

    private void classifyPromotedItems(String itemName, Integer itemCount) {
        if(!inventory.isItemInPromotion(itemName)) {
            Item promotedItem = inventory.getItemWithPromotion(itemName);
            scanItemInfo.updatePromotedItem(promotedItem, itemCount);
        }
    }
}
