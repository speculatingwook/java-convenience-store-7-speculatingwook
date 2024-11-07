package store.domain;

import java.util.Objects;

public class Item {
    private final String name;
    private final Integer price;
    private final boolean isPromotion;
    private final Integer promotionAmount;

    public Item(String name, Integer price) {
        validate(price);
        this.name = name;
        this.price = price;
        this.isPromotion = false;
        this.promotionAmount = 0;
    }

    public Item(String name, Integer price, Integer promotionAmount) {
        validate(price);
        this.name = name;
        this.price = price;
        this.isPromotion = true;
        validatePromotionAmount(promotionAmount);
        this.promotionAmount = promotionAmount;
    }

    private void validate(Integer price) {
        validatePrice(price);
    }

    private void validatePrice(Integer price) {
        if (price < 0) {
            throw new ArithmeticException("[ERROR] 금액은 음수가 불가능합니다.");
        }
    }

    private void validatePromotionAmount(Integer promotionAmount) {
        if (promotionAmount < 0) {
            throw new ArithmeticException("[CRITICAL] promotionAmount cannot be less than zero.");
        }
    }

    public boolean isLack(Integer requestQuantity) {
        return promotionAmount < requestQuantity;
    }

    public Integer getLackQuantity(Integer requestQuantity) {
        if(!isLack(requestQuantity)) {
            return requestQuantity - promotionAmount;
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPromotion() {
        return isPromotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name + isPromotion);
    }
}
