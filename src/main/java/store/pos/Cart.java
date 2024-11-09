package store.pos;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Cart {
    private final Map<String, Integer> requestItems;
    private static final Pattern REQUEST_INPUT_REGEX = Pattern.compile("\\[([가-힣\\w]+)-(\\d+)\\]");

    public Cart(String request) {
        validate(request);
        this.requestItems = parseRequestToMap(request);
    }

    private void validate(String request) {

    }

    private Map<String, Integer> parseRequestToMap(String data) {
        return Arrays.stream(data.split(","))
                .map(REQUEST_INPUT_REGEX::matcher)
                .filter(Matcher::find)
                .collect(Collectors.toMap(
                        m -> m.group(1),
                        m -> Integer.parseInt(m.group(2))
                ));
    }

    public Map<String, Integer> getCartItems() {
        return requestItems;
    }

}
