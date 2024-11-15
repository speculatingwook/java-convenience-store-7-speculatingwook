package store.payment;


import static store.util.parser.ConvenienceStoreParser.formatNumberWithComma;

public class Receipt {
    private final StringBuilder receiptContent;
    private final StringBuilder itemContent;
    private final StringBuilder promotionContent;
    private final StringBuilder totalContent;


    public Receipt() {
        this.receiptContent = new StringBuilder();
        this.itemContent = new StringBuilder();
        this.promotionContent = new StringBuilder();
        this.totalContent = new StringBuilder();
        init();
    }

    private void init() {
        itemContent.append("상품명\t\t수량\t금액").append("\n");
        promotionContent.append("===========증\t정=============").append("\n");
        totalContent.append("==============================").append("\n");
    }

    public void addItemContent(String itemName, int count, int price) {
        itemContent.append(itemName).append("\t\t").append(count).append("\t").append(price).append("\n");
    }

    public void addPromotionContent(String itemName, int count) {
        promotionContent.append(itemName).append("\t\t").append(count).append("\n");
    }

    public void addTotalPurchaseAmount(int totalCount, int totalPrice) {
        totalContent.append("총구매액").append("\t\t").append(totalCount).append("\t").append(totalPrice).append("\n");
    }

    public void addTotalDiscount(int totalPromotionDiscount, double totalMembershipDiscount) {
        totalContent.append("행사할인").append("\t\t").append("-").append(totalPromotionDiscount).append("\n");
        totalContent.append("멤버십할인").append("\t\t").append("-").append(formatNumberWithComma((int) totalMembershipDiscount)).append("\n");
    }

    public void addAmountToPay(double totalPrice) {
        totalContent.append("내실돈").append("\t\t").append(formatNumberWithComma((int) totalPrice)).append("\n");
    }

    public String issueReceipt() {
        receiptContent.append("===========").append("W").append("===========").append("\n");
        receiptContent.append(itemContent).append("\n");
        receiptContent.append(promotionContent).append("\n");
        receiptContent.append(totalContent).append("\n");
        return receiptContent.toString();
    }
}
