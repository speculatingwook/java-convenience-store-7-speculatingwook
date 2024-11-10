package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.stock.Promotion;

import static org.junit.jupiter.api.Assertions.*;

class PromotionTest {

    @Test
    @DisplayName("주어진 유효한 입력값으로 Promotion 객체가 생성된다")
    void testPromotionCreation_validInputs() {
        // given
        String name = "탄산2+1";
        String minimumBuyCount = "2";
        String offerCount = "1";
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";

        // when
        Promotion promotion = new Promotion(name, minimumBuyCount, offerCount, startDate, endDate);

        // then
        assertNotNull(promotion);
        assertEquals(Integer.valueOf(2), promotion.getMinimumBuyCount());
        assertEquals(Integer.valueOf(1), promotion.getOfferCount());
    }

    @Test
    @DisplayName("잘못된 날짜 형식이 입력되면 예외가 발생한다")
    void testPromotionCreation_invalidDateFormat() {
        // given
        String name = "MD추천상품";
        String minimumBuyCount = "1";
        String offerCount = "1";
        String startDate = "2024-01-32"; // 잘못된 날짜
        String endDate = "2024-12-31";

        // when, then
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            new Promotion(name, minimumBuyCount, offerCount, startDate, endDate);
        });

        assertEquals("[CRITICAL] class Promotion input type invalid: not a valid date", exception.getMessage());
    }

    @Test
    @DisplayName("수치 값이 잘못된 경우 예외가 발생한다")
    void testPromotionCreation_invalidNumericValue() {
        // given
        String name = "반짝할인";
        String minimumBuyCount = "abc";
        String offerCount = "1";
        String startDate = "2024-11-01";
        String endDate = "2024-11-30";

        // when, then
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            new Promotion(name, minimumBuyCount, offerCount, startDate, endDate);
        });

        assertEquals("[CRITICAL] class Promotion input type invalid: not integer", exception.getMessage());
    }

    @Test
    @DisplayName("유효한 날짜 범위일 때 Promotion이 유효하다고 판별된다")
    void testPromotionIsValid_validRange() {
        // given
        String name = "MD추천상품";
        String minimumBuyCount = "1";
        String offerCount = "1";
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";

        Promotion promotion = new Promotion(name, minimumBuyCount, offerCount, startDate, endDate);

        // when
        boolean result = promotion.isPromotionValid();

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("날짜 범위가 현재 날짜와 맞지 않으면 Promotion이 유효하지 않다고 판별된다")
    void testPromotionIsValid_invalidRange() {
        // given
        String name = "반짝할인";
        String minimumBuyCount = "1";
        String offerCount = "1";
        String startDate = "2024-11-01";
        String endDate = "2024-11-30";

        Promotion promotion = new Promotion(name, minimumBuyCount, offerCount, startDate, endDate);

        // when
        boolean result = promotion.isPromotionValid();

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("Promotion 이름이 맞으면 true를 반환한다")
    void testIsRightPromotion() {
        // given
        String name = "탄산2+1";
        String minimumBuyCount = "2";
        String offerCount = "1";
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";

        Promotion promotion = new Promotion(name, minimumBuyCount, offerCount, startDate, endDate);

        // when
        boolean result = promotion.isRightPromotion("탄산2+1");

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("Promotion 이름이 일치하지 않으면 false를 반환한다")
    void testIsRightPromotion_nameMismatch() {
        // given
        String name = "탄산2+1";
        String minimumBuyCount = "2";
        String offerCount = "1";
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";

        Promotion promotion = new Promotion(name, minimumBuyCount, offerCount, startDate, endDate);

        // when
        boolean result = promotion.isRightPromotion("MD추천상품");

        // then
        assertFalse(result);
    }
}