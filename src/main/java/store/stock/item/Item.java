package store.stock.item;

import store.ErrorCode;

import java.util.Objects;

public class Item {
    private final String name;
    private final Integer price;
    private final Promotion promotion;

    public Item(String name, String price, Promotion promotion) {
        validate(price, promotion);
        this.name = name;
        this.price = Integer.parseInt(price);
        this.promotion = promotion;
    }

    public Item(String name, String price) {
        validatePrice(price);
        this.name = name;
        this.price = Integer.parseInt(price);
        this.promotion = null;
    }

    private void validate(String price, Promotion promotion) {
        validateNumeric(price);
        validatePrice(price);
    }

    private void validateNumeric(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(ErrorCode.INVALID_FORMAT.getCriticalMessage());
        }
    }

    private void validatePrice(String price) {
        if (Integer.parseInt(price) < 0) {
            throw new IllegalArgumentException(ErrorCode.COUNT_UNDER_ZERO.getCriticalMessage());
        }
    }

    public String getName() {
        return name;
    }

    public Integer getPromotionOfferCount() {
        return promotion.getOfferCount();
    }

    public Integer getPromotionMinimumBuyCount() {
        return promotion.getMinimumBuyCount();
    }

    public Integer getPrice() {
        return price;
    }

    public boolean isPromotionPresent() {
        return promotion != null;
    }

    public boolean isPromotionEventValid() {
        if (promotion == null) {
            return false;
        }
        return promotion.isPromotionValid();
    }

    public String getPromotionName() {
        if (promotion == null) {
            return "";
        }
        return promotion.getPromotionName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name + promotion);
    }
}
