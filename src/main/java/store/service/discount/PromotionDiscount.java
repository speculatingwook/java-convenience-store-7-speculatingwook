package store.service.discount;

public interface PromotionDiscount {
    Integer calculatePromotionItemCount(String itemName, int requestCount);
}
