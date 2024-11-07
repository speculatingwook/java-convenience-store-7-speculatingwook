package store.domain;

import java.util.Objects;

public class PromotionItem extends Item {
    private final Integer promotionCount;

    public PromotionItem(Integer promotionCount, String name, Integer price, Integer quantity) {
        super(name, price, quantity);
        validate(promotionCount);
        this.promotionCount = promotionCount;
    }

    private void validate(Integer promotionCount) {
        if (promotionCount <= 0) {
            throw new ArithmeticException("promotionCount must be greater than 0");
        }
    }

    public boolean isLack(Integer requestQuantity) {
        return promotionCount < requestQuantity;
    }

    public Integer getLackQuantity(Integer requestQuantity) {
        if(!isLack(requestQuantity)) {
           return requestQuantity - promotionCount;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
