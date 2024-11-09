package store.domain;

import store.service.discount.Discount;

import java.util.Map;

//- [ ] 프로모션 할인을 진행하고, 영수증에 내용을 추가한다.
//- [ ] 멤버쉽할인 여부에 따라 멤버쉽 할인을 한다.
//- [ ] 구매 상품 내역, 증정 상품 내역, 금액 정보가 담긴 영수증을 발행한다.
//- [ ] 추가 구매 여부를 확인한다.
//- [ ] 인벤토리의 변경 내역을 반영한다.
public class ConvenienceStore {
    private final Inventory inventory;
    private final Receipt receipt;
    private final Discount discount;

    public ConvenienceStore(Inventory inventory, Receipt receipt, Discount discount) {
        this.inventory = inventory;
        this.receipt = receipt;
        this.discount = discount;
    }

    public boolean isRequestAmountCanOfferPromotion(String itemName, int count) {
        Item promotionItem = inventory.getItemWithPromotion(itemName);
        if(!promotionItem.isPromotionPresent()) {
            return false;
        }

        Integer minimumBuyCount = promotionItem.getPromotionMinimumBuyCount();
        Integer offerCount = promotionItem.getPromotionOfferCount();
        return count % (minimumBuyCount + offerCount) == minimumBuyCount;
    }

    public boolean isPromotionAmountLack(String itemName, int count) {
        Item promotionItem = inventory.getItemWithPromotion(itemName);
        if(!promotionItem.isPromotionPresent()) {
            return false;
        }
        // Discount 리팩토링 필요
        return discount.calculatePromotionItemCount(itemName, 0) >= count;
    }

    public void addItemsToCart(Map<String, Integer> requestItems) {
        for (Map.Entry<String, Integer> entry : requestItems.entrySet()) {
            String itemName = entry.getKey();
            Integer count = entry.getValue();
            addItemToCart(itemName, count);
        }
    }

    private void addItemToCart(String itemName, int count) {
        if(inventory.isItemInPromotion(itemName)) {
            addPromotionItemToCart(itemName, count);
        }
        if (!inventory.isItemInPromotion(itemName)) {
            addNonPromotionItemToCart(itemName, count);
        }
    }

    private void addNonPromotionItemToCart(String itemName, int count) {
        Item item = inventory.getItemWithoutPromotion(itemName);
        Integer itemPrice = item.getPrice();
        inventory.deductItem(itemName, count);
        receipt.addItemContent(itemName, count, count * itemPrice);
    }

    private void addPromotionItemToCart(String itemName, int count) {
        Item item = inventory.getItemWithPromotion(itemName);
        Integer itemPrice = item.getPrice();
        inventory.deductItem(itemName, count);
        receipt.addItemContent(itemName, count, count * itemPrice);
        receipt.addPromotionContent(itemName, count /item.getPromotionMinimumBuyCount());
    }

}