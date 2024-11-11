package store.stock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.stock.item.Item;
import store.stock.item.Promotion;

class ItemTest {

    @Test
    @DisplayName("프로모션이 있는 상태로 객체가 생성되어야 한다.")
    void shouldCreateItemWithPromotion() {
        // given
        Promotion promotion = new Promotion("탄산2+1", "2", "1", "2024-06-01", "2024-12-31");

        // when
        Item item = new Item("콜라", "1000", promotion);

        // then
        Assertions.assertEquals("콜라", item.getName());
        Assertions.assertEquals(1000, item.getPrice());
        Assertions.assertEquals(1, item.getPromotionOfferCount());
        Assertions.assertEquals(2, item.getPromotionMinimumBuyCount());
        Assertions.assertTrue(item.isPromotionPresent());
    }

    @Test
    @DisplayName("프로모션이 없는 상태로 객체가 생성되어야 한다.")
    void shouldCreateItemWithoutPromotion() {
        // given
        String itemName = "콜라";
        String itemPrice = "1000";

        // when
        Item item = new Item(itemName, itemPrice);

        // then
        Assertions.assertEquals(itemName, item.getName());
        Assertions.assertEquals(Integer.parseInt(itemPrice), item.getPrice());
        Assertions.assertFalse(item.isPromotionPresent());
    }

    @Test
    @DisplayName("가격이 음수이면 오류가 발생한다.")
    void shouldThrowExceptionWhenPriceIsNegative() {
        // given
        String negativePrice = "-1000";

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Item("환타", negativePrice));
    }

    @Test
    @DisplayName("가격이 숫자가 아니면 오류가 발생한다.")
    void shouldThrowExceptionWhenPriceIsNotNumeric() {
        // given
        String invalidPrice = "가격";

        // when, then
        Assertions.assertThrows(NumberFormatException.class, () -> new Item("사이다", invalidPrice));
    }

    @Test
    @DisplayName("프로모션이 유효하지 않은 경우 false를 반환한다.")
    void shouldReturnFalseWhenPromotionIsInvalid() {
        // given
        Promotion invalidPromotion = new Promotion("사은품", "3", "1", "2023-01-01", "2023-06-30");

        // when
        Item item = new Item("초콜릿", "2000", invalidPromotion);

        // then
        Assertions.assertFalse(item.isPromotionEventValid());
    }

    @Test
    @DisplayName("프로모션 이름이 올바르게 반환되어야 한다.")
    void shouldReturnPromotionName() {
        // given
        Promotion promotion = new Promotion("할인 이벤트", "5", "1", "2024-01-01", "2024-12-31");

        // when
        Item item = new Item("과자", "1500", promotion);

        // then
        Assertions.assertEquals("할인 이벤트", item.getPromotionName());
    }

    @Test
    @DisplayName("프로모션이 없는 경우 빈 문자열을 반환한다.")
    void shouldReturnEmptyStringWhenNoPromotion() {
        // given
        String itemName = "비타민";
        String itemPrice = "500";

        // when
        Item item = new Item(itemName, itemPrice);

        // then
        Assertions.assertEquals("", item.getPromotionName());
    }

    @Test
    @DisplayName("해시코드가 이름과 프로모션에 따라 생성되어야 한다.")
    void shouldGenerateHashCodeBasedOnNameAndPromotion() {
        // given
        Promotion promotion = new Promotion("할인 이벤트", "3", "1", "2024-01-01", "2024-12-31");
        Item item1 = new Item("음료수", "1200", promotion);
        Item item2 = new Item("음료수", "1200", promotion);

        // when
        int hashCode1 = item1.hashCode();
        int hashCode2 = item2.hashCode();

        // then
        Assertions.assertEquals(hashCode1, hashCode2);
    }
}
