package store.payment;

import store.io.YesNoOption;
import store.payment.discount.Discount;
import store.pos.OrderItemInfo;
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

    public String issueReceipt(YesNoOption option) {
        int promotionDiscountAmount = discount.receivePromotionDiscount(orderItemInfo.getPromotedItems());
        double membershipDiscountAmount = discount.receiveMembershipDiscount(orderItemInfo.getUnpromotedItems());
        addItemsToReceipt();
        addDiscountsToReceipt(option, promotionDiscountAmount, membershipDiscountAmount);

        receipt.addTotalPrice(getTotalCount(), getTotalAmount());
        return receipt.issueReceipt();
    }

    private void addItemsToReceipt() {
        addPromotedItemsToReceipt();
        addUnpromotedItemsToReceipt();
    }

    private void addDiscountsToReceipt(YesNoOption option, Integer discountAmount, Double membershipDiscountAmount) {
        boolean isMembership = option.getMembershipDiscount();
        if (isMembership) {
            addMembershipDiscountToReceipt(discountAmount, membershipDiscountAmount);
        }
        if (!isMembership) {
            addDefaultDiscountToReceipt(discountAmount);
        }
    }

    private void addMembershipDiscountToReceipt(Integer promotionDiscountAmount, Double membershipDiscountAmount) {
        receipt.addTotalDiscount(promotionDiscountAmount, membershipDiscountAmount);
        double totalPay = getTotalAmount() - promotionDiscountAmount - membershipDiscountAmount;
        receipt.addResult(totalPay);
    }

    private void addDefaultDiscountToReceipt(Integer promotionDiscountAmount) {
        receipt.addTotalDiscount(promotionDiscountAmount, 0);
        double totalPay = getTotalAmount() - promotionDiscountAmount;
        receipt.addResult(totalPay);
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

}
