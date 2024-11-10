package store.payment;

import store.io.StoreInput;
import store.payment.discount.Discount;
import store.pos.OrderItemInfo;
import store.pos.ScanItemInfo;
import store.stock.Item;
import java.util.Map;

public class Payment {
    private final Receipt receipt;
    private final Discount discount;
    private final OrderItemInfo orderItemInfo;

    public Payment(Receipt receipt, Discount discount, OrderItemInfo orderItemInfo) {
        this.receipt = receipt;
        this.discount = discount;
        this.orderItemInfo = orderItemInfo;
    }

    public Integer receivePromotionDiscount() {
        int totalDiscount = 0;
        Map<Item, Integer> promotedItems = orderItemInfo.getPromotedItems();
        for (Map.Entry<Item, Integer> entry : promotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            totalDiscount += item.getPrice() * quantity;
        }
        return totalDiscount;
    }

    public Double receiveMembershipDiscount() {
        int totalPrice = 0;
        Map<Item, Integer> unpromotedItems = orderItemInfo.getUnpromotedItems();
        for (Map.Entry<Item, Integer> entry : unpromotedItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;
        }
        return discount.calculateMembershipDiscount(totalPrice);
    }

    private Integer getTotalAmount() {
        int totalAmount = 0;
        Map<Item, Integer> orderItems = orderItemInfo.getOrderItems();
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            totalAmount += item.getPrice() * quantity;
        }
        return totalAmount;
    }

    private Integer getTotalCount() {
        int totalCount = 0;
        Map<Item, Integer> orderItems = orderItemInfo.getOrderItems();
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
        Map<Item, Integer> orderItems = orderItemInfo.getPromotedItems();
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            int offerAmount = quantity / (item.getPromotionMinimumBuyCount() + item.getPromotionOfferCount());
            receipt.addItemContent(item.getName(), quantity, item.getPrice());
            receipt.addPromotionContent(item.getName(), offerAmount);
        }
    }

    private void addUnpromotedItemsToReceipt() {
        Map<Item, Integer> orderItems = orderItemInfo.getUnpromotedItems();
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();
            receipt.addItemContent(item.getName(), quantity, item.getPrice());
        }
    }

}
