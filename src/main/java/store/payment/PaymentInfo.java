package store.payment;

import store.payment.discount.Discount;
import store.pos.OrderItemInfo;
import store.stock.item.Item;
import store.stock.item.Items;

import java.util.Map;

public class PaymentInfo {
    private final OrderItemInfo orderItemInfo;
    private final Discount discount;
    private final Items offerItems;
    private final Integer totalPurchaseAmount;
    private final Integer totalItemCounts;
    private final Integer promotionDiscountAmount;
    private Double membershipDiscountAmount;
    private Integer amountToPay;

    public PaymentInfo(OrderItemInfo orderItemInfo, Discount discount) {
        this.orderItemInfo = orderItemInfo;
        this.discount = discount;
        this.offerItems = discount.getOfferItems(orderItemInfo.getPromotedItems());
        this.totalPurchaseAmount = getTotalAmount(orderItemInfo.getOrderItems());
        this.totalItemCounts = getTotalItemCounts(orderItemInfo.getOrderItems());
        this.membershipDiscountAmount = 0.0;
        this.promotionDiscountAmount = calculatePromotionDiscountAmount(orderItemInfo, discount);
    }

    public Items getOfferItems() {
        return offerItems;
    }

    public Double getMembershipDiscountAmount() {
        return membershipDiscountAmount;
    }

    public Integer getPromotionDiscountAmount() {
        return promotionDiscountAmount;
    }

    public Integer getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public Integer getTotalItemCounts() {
        return totalItemCounts;
    }

    public Integer getAmountToPay() {
        return amountToPay;
    }

    public void applyMembership(boolean isMembership) {
        if (isMembership) {
            this.membershipDiscountAmount = calculateMembershipDiscountAmount(orderItemInfo, discount);
        }
        this.amountToPay = (int) (totalPurchaseAmount - membershipDiscountAmount - promotionDiscountAmount);
    }

    private Integer calculatePromotionDiscountAmount(OrderItemInfo orderItemInfo, Discount discount) {
        if (orderItemInfo.getPromotedItems().isEmpty()) {
            return 0;
        }
        return discount.receivePromotionDiscount(orderItemInfo.getPromotedItems());
    }

    private Double calculateMembershipDiscountAmount(OrderItemInfo orderItemInfo, Discount discount) {
        if (orderItemInfo.getUnpromotedItems().isEmpty()) {
            return 0.0;
        }
        return discount.receiveMembershipDiscount(orderItemInfo.getUnpromotedItems());
    }

    private Integer getTotalAmount(Items orderItems) {
        int totalAmount = 0;
        Map<Item, Integer> purchaseItems = orderItems.items();
        for (Item item : purchaseItems.keySet()) {
            totalAmount += item.getPrice() * purchaseItems.get(item);
        }
        return totalAmount;
    }

    private Integer getTotalItemCounts(Items orderItems) {
        int totalCount = 0;
        Map<Item, Integer> purchaseItems = orderItems.items();
        for (Item item : purchaseItems.keySet()) {
            totalCount += purchaseItems.get(item);
        }
        return totalCount;
    }

}
