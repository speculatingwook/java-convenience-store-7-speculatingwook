package store.domain;

import java.util.Objects;

public class Item {
    private final String name;
    private final Integer price;
    private final Promotion promotion;
    private final Integer promotionOfferCount;

    public Item(String name, Integer price, Promotion promotion) {
        validate(price, promotion);
        this.name = name;
        this.price = price;
        this.promotion = promotion;
        this.promotionOfferCount = promotion.getOfferCount();
    }

    public Item(String name, Integer price){
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.promotion = null;
        this.promotionOfferCount = 0;
    }

    private void validate(Integer price, Promotion promotion) {
        validatePrice(price);
        validatePromotionOffer(promotion.getOfferCount());
    }

    private void validatePrice(Integer price) {
        if (price < 0) {
            throw new ArithmeticException("[ERROR] 금액은 음수가 불가능합니다.");
        }
    }

    private void validatePromotionOffer(Integer promotionAmount) {
        if (promotionAmount < 0) {
            throw new ArithmeticException("[CRITICAL] promotionAmount cannot be less than zero.");
        }
    }

    public String getName() {
        return name;
    }

    public Integer getPromotionOfferCount() {
        return promotionOfferCount;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean isPromotionPresent() {
        return promotion != null;
    }


    @Override
    public int hashCode() {
        return Objects.hash(name + promotion);
    }
}
