package store.payment;

import store.io.YesNoOption;
import store.payment.discount.Discount;
import store.pos.OrderItemInfo;
import store.stock.Item;
import store.stock.Items;

import java.util.Map;

public class Payment {
    private final Receipt receipt;
    private final Discount discount;
    private OrderItemInfo orderItemInfo;
    private PaymentInfo paymentInfo;

    public Payment(Receipt receipt, Discount discount) {
        this.receipt = receipt;
        this.discount = discount;
    }

    public void receiveOrderItemInfo(OrderItemInfo orderItemInfo) {
        this.orderItemInfo = orderItemInfo;
        this.paymentInfo = new PaymentInfo(orderItemInfo, discount);
    }

    public String issueReceipt(YesNoOption option) {
        addPurchaseItemsToReceipt();
        applyPromotions();
        applyMembership(option);
        addTotalResults();

        return receipt.issueReceipt();
    }

    private void addPurchaseItemsToReceipt() {
        Map<Item, Integer> orderItems = orderItemInfo.getOrderItems().items();
        for (Item item : orderItems.keySet()) {
            receipt.addItemContent(item.getName(), orderItems.get(item), item.getPrice());
        }
    }

    private void applyPromotions() {
        Items offerItems = paymentInfo.getOfferItems();
        for (Item item : offerItems.items().keySet()) {
            receipt.addPromotionContent(item.getName(), offerItems.items().get(item));
        }
    }

    private void applyMembership(YesNoOption option) {
        boolean isMembership = option.getMembershipDiscount();
        paymentInfo.applyMembership(isMembership);
    }

    private void addTotalResults() {
        receipt.addTotalPurchaseAmount(paymentInfo.getTotalItemCounts(), paymentInfo.getTotalPurchaseAmount());
        receipt.addTotalDiscount(paymentInfo.getPromotionDiscountAmount(), paymentInfo.getMembershipDiscountAmount());
        receipt.addAmountToPay(paymentInfo.getAmountToPay());
    }

}
