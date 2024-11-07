package store.io;

public class StoreView {

    public void greet() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printCurrentStock(String currentStock) {
        System.out.println(currentStock);
    }

    public void guideForRequestFormat() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
    }

    public void guideForNoPromotion(String stockName, int count) {
        System.out.println("현재 "+ stockName +" "+ count +"개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }

    public void guideForMoreBenefit(String item) {
        System.out.println("현재 "+ item +"은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public void guideForMembershipDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public void guideForMoreRequests() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }

    public void printReceipt(String receipt) {
        System.out.println(receipt);
    }
}
