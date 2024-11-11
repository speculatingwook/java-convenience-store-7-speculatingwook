package store.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.io.StoreInput;
import store.io.StoreView;
import store.io.YesNoOption;
import store.payment.discount.ConvenienceStoreDiscount;
import store.payment.discount.Discount;
import store.pos.OrderItemInfo;
import store.stock.Item;
import store.stock.Promotion;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentTest {
    private Payment payment;
    private Receipt receipt;
    private Discount discount;
    private OrderItemInfo orderItemInfo;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
        discount = new ConvenienceStoreDiscount(10); // 10% 할인
        payment = new Payment(receipt, discount);
        promotion = new Promotion("1+1", "1", "1", "2021-01-01", "2099-01-01");

        // OrderItemInfo 초기화
        Map<Item, Integer> promotedItems = new HashMap<>();
        Map<Item, Integer> unpromotedItems = new HashMap<>();
        orderItemInfo = new OrderItemInfo(promotedItems, unpromotedItems);
    }

    @Test
    @DisplayName("1+1 할인 상품만 구매시 영수증이 정상 발행된다")
    void issueReceiptWithOnlyPromotionItems() {
        // given
        Item snack = new Item("과자", "1000", promotion);
        orderItemInfo.updatePromotedItem(snack, 2);
        payment.receiveOrderItemInfo(orderItemInfo);

        TestStoreInput testInput = new TestStoreInput(false);
        YesNoOption yesNoOption = new YesNoOption(testInput);

        // when
        String receipt = payment.issueReceipt(yesNoOption);

        // then
        assertThat(receipt).contains("과자\t\t2\t1000");
        assertThat(receipt).contains("과자\t\t1"); // 1+1 프로모션으로 1개 증정
        assertThat(receipt).contains("행사할인\t\t-1000"); // 1개 금액만큼 할인
        assertThat(receipt).contains("멤버십할인\t\t-0"); // 멤버십 할인 미적용
        assertThat(receipt).contains("총구매액\t\t2\t2000"); // 총 수량 2개, 금액 2000원
    }

    @Test
    @DisplayName("일반 상품만 구매시 멤버십 할인이 정상 적용된다")
    void issueReceiptWithOnlyUnpromotedItems() {
        // given
        Item drink = new Item("음료", "2000");
        orderItemInfo.updateUnPromotedItem(drink, 1);
        payment.receiveOrderItemInfo(orderItemInfo);

        TestStoreInput testInput = new TestStoreInput(true); // 멤버십 적용
        YesNoOption yesNoOption = new YesNoOption(testInput);

        // when
        String receipt = payment.issueReceipt(yesNoOption);

        // then
        assertThat(receipt).contains("음료\t\t1\t2000");
        assertThat(receipt).contains("행사할인\t\t-0"); // 프로모션 할인 없음
        assertThat(receipt).contains("멤버십할인\t\t-200"); // 10% 할인 적용
        assertThat(receipt).contains("총구매액\t\t1\t2000"); // 총 수량 1개, 금액 2000원
    }

    @Test
    @DisplayName("프로모션 상품과 일반 상품을 함께 구매시 모든 할인이 정상 적용된다")
    void issueReceiptWithMixedItems() {
        // given
        Item snack = new Item("과자", "1000", promotion);
        Item drink = new Item("음료", "2000");

        orderItemInfo.updatePromotedItem(snack, 2);
        orderItemInfo.updateUnPromotedItem(drink, 1);
        payment.receiveOrderItemInfo(orderItemInfo);

        TestStoreInput testInput = new TestStoreInput(true);
        YesNoOption yesNoOption = new YesNoOption(testInput);

        // when
        String receipt = payment.issueReceipt(yesNoOption);

        // then
        assertThat(receipt).contains("과자\t\t2\t1000");
        assertThat(receipt).contains("음료\t\t1\t2000");
        assertThat(receipt).contains("과자\t\t1"); // 1+1 프로모션으로 1개 증정
        assertThat(receipt).contains("행사할인\t\t-1000"); // 과자 1개 금액 할인
        assertThat(receipt).contains("멤버십할인\t\t-200"); // 음료에 대한 10% 할인
        assertThat(receipt).contains("총구매액\t\t3\t4000"); // 총 수량 3개, 금액 4000원
    }

    // YesNoOption 테스트를 위한 StoreInput 구현체
    private static class TestStoreInput extends StoreInput {
        private final boolean isMembership;

        public TestStoreInput(boolean isMembership) {
            super(new StoreView());
            this.isMembership = isMembership;
        }

        @Override
        public String readMembershipDiscountRequest() {
            return isMembership ? "Y" : "N";
        }

        @Override
        public String readMoreStockForDiscountRequest(String itemName) {
            return "N";
        }

        @Override
        public String readAdditionalItemRequest() {
            return "N";
        }
    }
}