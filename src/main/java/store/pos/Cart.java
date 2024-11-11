package store.pos;

import store.ErrorCode;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Cart {
    private static final Pattern IS_FORMAT_CORRECT = Pattern.compile("^\\[([가-힣\\w]+)-(\\d+)](,\\[([가-힣\\w]+)-(\\d+)])*$");
    private static final Pattern PARSE_REQUEST_REGEX = Pattern.compile("\\[([가-힣\\w]+)-(\\d+)]");
    private final Map<String, Integer> cartItems;

    public Cart(String itemRequest) {
        if (!IS_FORMAT_CORRECT.matcher(itemRequest).matches()) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_INPUT.getErrorMessage());
        }
        this.cartItems = putItemsToCart(itemRequest);
    }

    public Map<String, Integer> getCartItems() {
        return cartItems;
    }

    private Map<String, Integer> putItemsToCart(String itemRequest) {
        return Arrays.stream(itemRequest.split(","))
                .map(PARSE_REQUEST_REGEX::matcher)
                .filter(Matcher::find)
                .collect(Collectors.toMap(
                        m -> m.group(1),
                        m -> Integer.parseInt(m.group(2))
                ));
    }
}
