package store.domain;

public class Receipt {
    private final StringBuilder receiptContent;

    public Receipt() {
        this.receiptContent = new StringBuilder();
    }

    public void addReceiptContent(String content) {
        receiptContent.append(content);
    }

    @Override
    public String toString() {
        return receiptContent.toString();
    }
}
