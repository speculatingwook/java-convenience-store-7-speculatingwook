package store.payment.discount;

public interface PromotionDiscount {
    Integer calculatePromotionItemCount(String itemName, int requestCount);
}
