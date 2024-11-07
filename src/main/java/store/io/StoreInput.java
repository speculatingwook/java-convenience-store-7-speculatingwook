package store.io;

import camp.nextstep.edu.missionutils.Console;

public class StoreInput {
    private final StoreView view;

    public StoreInput(StoreView view) {
        this.view = view;
    }

    public String readItemRequest() {
        view.greet();
        view.guideForRequestFormat();
        return Console.readLine();
    }

    public String readMoreStockForDiscountRequest(String stockName) {
        view.guideForMoreBenefit(stockName);
        return Console.readLine();
    }

    public String readFullPricePaymentRequest(String stockName, int count) {
        view.guideForNoPromotion(stockName, count);
        return Console.readLine();
    }

    public String readMembershipDiscountRequest() {
        view.guideForMembershipDiscount();
        return Console.readLine();
    }

    public String readAdditionalItemRequest() {
        view.guideForMoreRequests();
        return Console.readLine();
    }
}
