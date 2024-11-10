package store;

public enum ErrorCode {
    Y_N_INPUT_INVALID("입력이 잘못되었습니다. Y 혹은 N을 입력해주세요",601),
    INVALID_FORMAT_INPUT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.", 701),
    OTHER_INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요.", 702),
    NO_ITEM("존재하지 않는 상품입니다. 다시 입력해 주세요.", 801),
    EXCEED_ITEM_COUNT("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.", 802),
    COUNT_UNDER_ZERO("the amount cannot be negative.", 1001),
    INVALID_FORMAT("the format is invalid.", 1002),
    INVALID_DATE_FORMAT("invalid date.", 1003);

    private final String errorMessage;
    private final Integer code;
    private static final String ERROR = "[ERROR]";
    private static final String CRITICAL = "[CRITICAL]";

    ErrorCode(String errorMessage, Integer code){
        this.errorMessage = errorMessage;
        this.code = code;
    }

    public String getErrorMessage() {
        return ERROR + " " + errorMessage;
    }

    public String getCriticalMessage() {
        return CRITICAL + " " + errorMessage;
    }
}
