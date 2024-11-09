package store.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.Promotion;
import store.stock.Item;


class ItemTest {

    @Test
    @DisplayName("프로모션이 있는 상태로 객체가 생성되어야 한다.")
    void shouldCreateItemWithPromotion() {
        Promotion promotion = new Promotion("탄산2+1", "2", "1", "2024-06-01", "2024-12-31");
        Item item = new Item("콜라", "1000", promotion);

        Assertions.assertEquals("콜라", item.getName());
        Assertions.assertEquals(1000, item.getPrice());
        Assertions.assertEquals(1, item.getPromotionOfferCount());
        Assertions.assertEquals(2, item.getPromotionMinimumBuyCount());
        Assertions.assertTrue(item.isPromotionPresent());
    }

    @Test
    @DisplayName("프로모션이 없는 상태로 객체가 생성되어야 한다.")
    void shouldCreateItemWithoutPromotion() {
        Item item = new Item("콜라", "1000");

        Assertions.assertEquals("콜라", item.getName());
        Assertions.assertEquals(1000, item.getPrice());
        Assertions.assertFalse(item.isPromotionPresent());
    }

    @Test
    @DisplayName("가격이 음수이면 오류가 발생한다.")
    void shouldThrowExceptionWhenPriceIsNegative() {
        Assertions.assertThrows(ArithmeticException.class, () -> new Item("환타", "-10000"));
    }

    @Test
    @DisplayName("사은품이 음수이면 오류가 발생한다.")
    void shouldThrowExceptionWhenPromotionOfferCountIsNegative() {
        Promotion promotion = new Promotion("MD추천", "2", "-1", "2023-12-01", "2023-12-31");
        Assertions.assertThrows(ArithmeticException.class, () -> new Item("아이스크림", "3000", promotion));
    }
}