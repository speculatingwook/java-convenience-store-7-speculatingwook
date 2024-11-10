package store.util.parser;


import store.stock.dto.PromotionDto;

import java.util.List;

public interface PromotionParser {
    PromotionDto parseToPromotionDto(String text);
    List<PromotionDto> parseToPromotionDtos(String text);
}
