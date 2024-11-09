package store;

import java.util.List;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Promotion getPromotion(String name) {
        return promotions.stream().filter(promotion -> promotion.isRightPromotion(name)).findFirst().orElse(null);
    }
}
