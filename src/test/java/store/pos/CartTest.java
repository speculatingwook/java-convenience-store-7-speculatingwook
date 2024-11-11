package store.pos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.ErrorCode;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {
    private Cart validCart;

    @BeforeEach
    void setUp() {
        validCart = new Cart("[음료수-3],[과자-2]");
    }

    @Test
    @DisplayName("올바른 형식의 장바구니 요청은 정상적으로 처리된다")
    void testValidCartRequest() {
        // given, when
        Map<String, Integer> items = validCart.getCartItems();

        // then
        assertEquals(2, items.size());
        assertEquals(3, items.get("음료수"));
        assertEquals(2, items.get("과자"));
    }

    @Test
    @DisplayName("잘못된 형식의 장바구니 요청은 예외를 던진다")
    void testInvalidCartRequest() {
        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Cart("[음료수-3,][과자-2]"));
        assertEquals(ErrorCode.INVALID_FORMAT_INPUT.getErrorMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("아이템 요청 형식에 공백이 포함되어 있으면 예외를 던진다")
    void testCartRequestWithWhitespace() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Cart("[음료수-3] , [과자-2]"));
        assertEquals(ErrorCode.INVALID_FORMAT_INPUT.getErrorMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("장바구니 요청에서 아이템이 하나만 있을 때도 정상적으로 처리된다")
    void testSingleItemCartRequest() {
        // given
        Cart singleItemCart = new Cart("[음료수-1]");

        // when
        Map<String, Integer> items = singleItemCart.getCartItems();

        // then
        assertEquals(1, items.size());
        assertEquals(1, items.get("음료수"));
    }

    @Test
    @DisplayName("아이템 이름에 특수문자가 포함되면 예외를 던진다")
    void testCartRequestWithSpecialCharacters() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Cart("[음료수$-3]"));
        assertEquals(ErrorCode.INVALID_FORMAT_INPUT.getErrorMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("장바구니 아이템 수가 올바르게 처리되는지 확인한다")
    void testCartRequestItemCount() {
        // given
        Cart cart = new Cart("[음료수-5],[과자-3],[사탕-2]");

        // when
        Map<String, Integer> items = cart.getCartItems();

        // then
        assertEquals(3, items.size());
        assertEquals(5, items.get("음료수"));
        assertEquals(3, items.get("과자"));
        assertEquals(2, items.get("사탕"));
    }
}
