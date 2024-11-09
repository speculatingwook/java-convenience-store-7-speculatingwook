package store.domain;

import store.dto.ItemDto;
import store.io.StoreInput;
import store.io.writer.Writer;
import store.service.discount.Discount;
import store.service.parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvenienceStore {
    private final Inventory inventory;
    private final Receipt receipt;
    private final Discount discount;
    private final Writer writer;
    private final Parser parser;
    private final StoreInput input;
    private final Map<String, Integer> requestItems;
    private final Map<String, Integer> promotionItems;
    private final Map<String, Integer> nonPromotionItems;

    public ConvenienceStore(Inventory inventory, Receipt receipt, Discount discount, Writer writer, Parser parser, StoreInput input) {
        this.inventory = inventory;
        this.receipt = receipt;
        this.discount = discount;
        this.writer = writer;
        this.parser = parser;
        this.input = input;
        this.requestItems = new HashMap<>();
        this.promotionItems = new HashMap<>();
        this.nonPromotionItems = new HashMap<>();
    }

    public void scan(Map<String, Integer> requestItems) {
        this.requestItems.putAll(requestItems);
        for (Map.Entry<String, Integer> entry : requestItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            if (isRequestAmountCanOfferPromotion(itemName, quantity)) {
                String isAdditionalRequest = input.readMoreStockForDiscountRequest(itemName);
                offerMoreItemWhenPromotionCanbeOffered(isAdditionalRequest, itemName);
            }
            if(isPromotionAmountLack(itemName, quantity)) {
                String anotherInputThatReceiveYorN = input.readFullPricePaymentRequest(itemName, quantity - discount.calculatePromotionItemCount(itemName, quantity));
                executeWhenPromotionLack(anotherInputThatReceiveYorN, itemName, quantity);
            }
        }
    }

    private void offerMoreItemWhenPromotionCanbeOffered(String yesOrNo, String itemName) {
        Item promotionItem = inventory.getItemWithPromotion(itemName);
        if(yesOrNo.equals("Y") && promotionItem != null) {
            requestItems.put(itemName, requestItems.get(itemName) + promotionItem.getPromotionOfferCount());
        }
    }

    private void executeWhenPromotionLack(String yesOrNo, String itemName, Integer quantity) {
        if(yesOrNo.equals("Y")) {
            Integer totalStock = inventory.getTotalAmount(itemName);
            Integer nonPromotionAmount = totalStock - discount.calculatePromotionItemCount(itemName, quantity);
            putNonPromotionItem(itemName, nonPromotionAmount);
        }
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

    public void addItemsToCart() {
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
        putNonPromotionItem(itemName, count);
        receipt.addItemContent(itemName, count, count * itemPrice);
    }

    private void addPromotionItemToCart(String itemName, int count) {
        Item item = inventory.getItemWithPromotion(itemName);
        Integer itemPrice = item.getPrice();
        int promotionCount = count / item.getPromotionMinimumBuyCount();
        int promotionTotalCount = promotionCount * item.getPromotionMinimumBuyCount();
        inventory.deductItem(itemName, count);
        putPromotionItem(itemName, promotionCount);
        receipt.addItemContent(itemName, count, count * itemPrice);
        receipt.addPromotionContent(itemName, promotionTotalCount);
    }

    private void putNonPromotionItem(String itemName, int count) {
        if(nonPromotionItems.containsKey(itemName)) {
            nonPromotionItems.put(itemName, nonPromotionItems.get(itemName) + count);
        }
        if(!nonPromotionItems.containsKey(itemName)) {
            nonPromotionItems.put(itemName, count);
        }
    }

    private void putPromotionItem(String itemName, int count) {
        if(promotionItems.containsKey(itemName)) {
            promotionItems.put(itemName, promotionItems.get(itemName) + count);
        }
        if(!promotionItems.containsKey(itemName)) {
            promotionItems.put(itemName, count);
        }
    }

    private Integer getTotalAmount() {
        int totalAmount = 0;
        for (Map.Entry<String, Integer> entry : requestItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            totalAmount += inventory.getItem(itemName).getPrice() * quantity;
        }
        return totalAmount;
    }

    private Integer getTotalCount() {
        int totalCount = 0;
        for (Map.Entry<String, Integer> entry : requestItems.entrySet()) {
            Integer quantity = entry.getValue();
            totalCount += quantity;
        }
        return totalCount;
    }

    public void issueReceipt() {
        int totalAmount = getTotalAmount();
        int totalCount = getTotalCount();
        int promotionDiscountAmount = receivePromotionDiscount();
        double membershipDiscountAmount = receiveMembershipDiscount();
        double totalPay = totalAmount - promotionDiscountAmount - membershipDiscountAmount;
        receipt.addTotalPrice(getTotalCount(), getTotalAmount());
        receipt.addTotalDiscount(receivePromotionDiscount(), receiveMembershipDiscount());
        receipt.addResult(totalPay);
        writeChangedContent();
    }

    private void writeChangedContent() {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : requestItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            if(inventory.isItemInPromotion(itemName)) {
                Item item = inventory.getItemWithPromotion(itemName);
                itemDtos.add(new ItemDto(itemName, String.valueOf(item.getPrice()), String.valueOf(quantity),item.getPromotionName()));
            }
            if(!inventory.isItemInPromotion(itemName)) {
                Item item = inventory.getItemWithoutPromotion(itemName);
                itemDtos.add(new ItemDto(itemName, String.valueOf(item.getPrice()), String.valueOf(quantity),null));
            }
        }

        String content = parser.parseItemDtosToText(itemDtos);
        writer.write("products.md", content);
    }


    public Integer receivePromotionDiscount() {
        int totalDiscount = 0;
        for (Map.Entry<String, Integer> entry : promotionItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            totalDiscount += inventory.getItem(itemName).getPrice() * quantity;
        }
        return totalDiscount;
    }


    public Double receiveMembershipDiscount() {
        int totalPrice = 0;
        for (Map.Entry<String, Integer> entry : nonPromotionItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            totalPrice += inventory.getItem(itemName).getPrice() * quantity;
        }
        return discount.calculateMembershipDiscount(totalPrice);
    }

}