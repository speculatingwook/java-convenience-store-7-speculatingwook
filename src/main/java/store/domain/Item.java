package store.domain;

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

    public Item(String name, String price){
        validatePrice(price);
        this.name = name;
        this.price = Integer.parseInt(price);
        this.promotion = null;
    }

    private void validate(String price, Promotion promotion) {
        validateNumeric(price);
        validatePrice(price);
        validatePromotionOffer(promotion.getOfferCount());
    }

    private void validateNumeric(String input) {
        try{
            Integer.parseInt(input);
        }catch (NumberFormatException e){
            throw new NumberFormatException(input);
        }
    }

    private void validatePrice(String price) {
        if (Integer.parseInt(price) < 0) {
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


    @Override
    public int hashCode() {
        return Objects.hash(name + promotion);
    }
}
