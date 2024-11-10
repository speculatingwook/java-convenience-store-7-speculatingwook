package store.payment;

import store.io.StoreInput;
import store.payment.discount.Discount;
import store.pos.ScanItemInfo;
import store.stock.Item;
import java.util.Map;

public class Payment {
    private final Receipt receipt;
    private final Discount discount;
    private ScanItemInfo scanItemInfo;

    public Payment(Receipt receipt, Discount discount) {
        this.receipt = receipt;
        this.discount = discount;
    }

    public void receiveData(ScanItemInfo scanItemInfo) {
        this.scanItemInfo = scanItemInfo;
    }

    public Integer receivePromotionDiscount() {
        int totalDiscount = 0;
        Map<Item, Integer> promotedItems = scanItemInfo.getPromotedItems();
        for (Map.Entry<Item, Integer> entry : promotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            totalDiscount += item.getPrice() * quantity;
        }
        return totalDiscount;
    }

    public Double receiveMembershipDiscount() {
        int totalPrice = 0;
        Map<Item, Integer> unpromotedItems = scanItemInfo.getUnpromotedItems();
        for (Map.Entry<Item, Integer> entry : unpromotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;
        }
        return discount.calculateMembershipDiscount(totalPrice);
    }

    private Integer getTotalAmount() {
        int totalAmount = 0;
        Map<Item, Integer> orderItems = scanItemInfo.getOrderItems();
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            totalAmount += item.getPrice() * quantity;
        }
        return totalAmount;
    }

    private Integer getTotalCount() {
        int totalCount = 0;
        Map<Item, Integer> orderItems = scanItemInfo.getOrderItems();
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Integer quantity = entry.getValue();
            totalCount += quantity;
        }
        return totalCount;
    }

    public String issueReceipt(StoreInput input) {
        String membership = input.readMembershipDiscountRequest();
        addPromotedItemsToReceipt();
        addUnpromotedItemsToReceipt();
        addMembershipDiscountToReceipt(membership);
        addNoMembershipDiscountToReceipt(membership);

        receipt.addTotalPrice(getTotalCount(), getTotalAmount());
        return receipt.issueReceipt();
    }

    private void addMembershipDiscountToReceipt(String isMembership) {
        if(isMembership.equals("Y")){
            receipt.addTotalDiscount(receivePromotionDiscount(), receiveMembershipDiscount());
            double totalPay = getTotalAmount() - receivePromotionDiscount() - receiveMembershipDiscount();
            receipt.addResult(totalPay);
        }
    }

    private void addNoMembershipDiscountToReceipt(String isMembership) {
        if(isMembership.equals("N")){
            receipt.addTotalDiscount(receivePromotionDiscount(), 0);
            double totalPay = getTotalAmount() - receivePromotionDiscount();
        }
    }

    private void addPromotedItemsToReceipt() {
        Map<Item, Integer> orderItems = scanItemInfo.getPromotedItems();
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            int offerAmount = quantity / (item.getPromotionMinimumBuyCount() + item.getPromotionOfferCount());
            receipt.addItemContent(item.getName(), quantity, item.getPrice());
            receipt.addPromotionContent(item.getName(), offerAmount);
        }
    }

    private void addUnpromotedItemsToReceipt() {
        Map<Item, Integer> orderItems = scanItemInfo.getUnpromotedItems();
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            receipt.addItemContent(item.getName(), quantity, item.getPrice());
        }
    }

}
