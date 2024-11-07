package store.domain;

import java.util.Objects;

public class Item {
    private final String name;
    private final Integer price;
    private Integer quantity;

    public Item(String name, Integer price, Integer quantity) {
        validate(price, quantity);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    public void reduceQuantity(Integer quantity) {
        validateQuantity(this.quantity - quantity);
        this.quantity = this.quantity - quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
