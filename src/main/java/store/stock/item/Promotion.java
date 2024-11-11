package store.stock.item;


import camp.nextstep.edu.missionutils.DateTimes;
import store.ErrorCode;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Promotion {
    private final String name;
    private final Integer minimumBuyCount;
    private final Integer offerCount;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private static final Pattern isDateFormat = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");

    public Promotion(String name, String minimumBuyCount, String offerCount, String startDate, String endDate) {
        validate(minimumBuyCount, offerCount, startDate, endDate);
        this.name = name;
        this.minimumBuyCount = Integer.parseInt(minimumBuyCount);
        this.offerCount = Integer.parseInt(offerCount);
        this.startDate = parseDate(startDate);
        this.endDate = parseDate(endDate);
    }

    private LocalDateTime parseDate(String date) {
        List<Integer> parsedDate = Arrays.stream(date.split("-")).map(Integer::parseInt).toList();
        return LocalDateTime.of(parsedDate.get(0), parsedDate.get(1), parsedDate.get(2), 0, 0);
    }

    private void validate(String minimumBuyCount, String offerCount, String startDate, String endDate) {
        validateNumeric(minimumBuyCount);
        validateNumeric(offerCount);
        validateDateFormat(startDate);
        validateDateFormat(endDate);
    }

    private void validateNumeric(String count) {
        try {
            Integer.parseInt(count);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT.getCriticalMessage());
        }
    }

    private void validateDateFormat(String date) {
        if (!isDateFormat.matcher(date).matches()) {
            throw new IllegalArgumentException(ErrorCode.INVALID_DATE_FORMAT.getCriticalMessage());
        }
    }

    public boolean isPromotionValid() {
        return DateTimes.now().isAfter(startDate) && DateTimes.now().isBefore(endDate);
    }

    public boolean isRightPromotion(String name) {
        return this.name.equals(name);
    }

    public Integer getMinimumBuyCount() {
        return minimumBuyCount;
    }

    public Integer getOfferCount() {
        return offerCount;
    }

    public String getPromotionName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}