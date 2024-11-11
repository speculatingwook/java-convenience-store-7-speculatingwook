package store.pos;

import store.stock.Inventory;
import store.stock.item.Item;

import java.util.Map;

/**
 * 제품을 스캔하고, 제품에 대한 정보를 처리하여 알려주는 역할
 */
public class PosScanner {
    private final Inventory inventory;
    private final ScanItemInfo scanItemInfo;

    public PosScanner(Inventory inventory) {
        this.inventory = inventory;
        this.scanItemInfo = new ScanItemInfo();
    }

    public ScanItemInfo scanItems(Map<String, Integer> items) {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemName = entry.getKey();
            Integer itemCount = entry.getValue();
            classifyUnpromotedItems(itemName, itemCount);
            classifyPromotedItems(itemName, itemCount);
        }
        return scanItemInfo;
    }

    private void classifyUnpromotedItems(String itemName, Integer itemCount) {
        if (inventory.isItemInPromotion(itemName)) {
            Item promotedItem = inventory.getItemWithPromotion(itemName);
            updatePromotedItemWhenValid(promotedItem, itemCount);
            updateUnPromotedItemWhenNotValid(promotedItem, itemCount);
        }
    }

    private void classifyPromotedItems(String itemName, Integer itemCount) {
        if (!inventory.isItemInPromotion(itemName)) {
            Item promotedItem = inventory.getItemWithoutPromotion(itemName);
            scanItemInfo.updateUnPromotedItem(promotedItem, itemCount);
        }
    }

    private void updatePromotedItemWhenValid(Item promotedItem, Integer itemCount) {
        if (promotedItem.isPromotionEventValid()){
            scanItemInfo.updatePromotedItem(promotedItem, itemCount);
        }
    }

    private void updateUnPromotedItemWhenNotValid(Item promotedItem, Integer itemCount) {
        if (!promotedItem.isPromotionEventValid()){
            scanItemInfo.updateUnPromotedItem(promotedItem, itemCount);
        }
    }
}
