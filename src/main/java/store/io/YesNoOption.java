package store.io;

import java.util.Objects;

public class YesNoOption {
    private static final String YES = "Y";
    private static final String NO = "N";
    private final StoreInput input;

    public YesNoOption(StoreInput input) {
        this.input = input;
    }

    private boolean isYes(String option) {
        validate(option);
        return Objects.equals(option, YES);
    }

    public boolean isMoreStockRequest(String itemName) {
        while (true) {
            try {
                String option = input.readMoreStockForDiscountRequest(itemName);
                return isYes(option);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean confirmPayWithNoPromotion(String itemName, Integer amount) {
        while (true) {
            try {
                String option = input.readFullPricePaymentRequest(itemName, amount);
                return isYes(option);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean getMembershipDiscount() {
        while (true) {
            try {
                String option = input.readMembershipDiscountRequest();
                return isYes(option);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean buyMoreItems() {
        while (true) {
            try {
                String option = input.readAdditionalItemRequest();
                return isYes(option);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validate(String input) {
        if (!(input.equals(YES) || input.equals(NO))) {
            throw new IllegalArgumentException("[ERROR] 입력이 잘못되었습니다. Y 혹은 N을 입력해주세요");
        }
    }

}
