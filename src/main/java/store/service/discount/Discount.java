package store.service.discount;

public interface Discount extends MembershipDiscount, PromotionDiscount {
    Integer calculatePromotionItemCount(String itemName, int requestCount);

    Double calculateMembershipDiscount(int amount);
}
