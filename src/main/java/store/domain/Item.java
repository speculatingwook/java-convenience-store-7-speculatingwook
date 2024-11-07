package store.domain;

import java.util.Objects;

public class Item {
    private final String name;
    private final Integer price;
    private final boolean isPromotion;
    private final Integer promotionAmount;
    private Integer quantity;

    public Item(String name, Integer price, Integer quantity) {
        validate(price, quantity);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isPromotion = false;
        this.promotionAmount = 0;
    }

    public Item(String name, Integer price, Integer quantity, Integer promotionAmount) {
        validate(price, quantity);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isPromotion = true;
        validatePromotionAmount(promotionAmount);
        this.promotionAmount = promotionAmount;
    }

    private void validate(Integer price, Integer quantity) {
        validatePrice(price);
        validateQuantity(quantity);
    }

    private void validateQuantity(Integer quantity) {
        if (quantity < 0) {
            throw new ArithmeticException("[ERROR] 수량은 음수가 불가능합니다.");
        }
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

    public Integer getQuantity() {
        return Integer.valueOf(quantity);
    }

    public void reduceQuantity(Integer quantity) {
        validateQuantity(this.quantity - quantity);
        this.quantity = this.quantity - quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name + isPromotion);
    }
}
