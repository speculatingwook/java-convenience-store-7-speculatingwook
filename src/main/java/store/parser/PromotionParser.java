package store.parser;


import store.dto.PromotionDto;

import java.util.List;

public interface PromotionParser {
    PromotionDto parseToPromotionDto(String text);
    List<PromotionDto> parseToPromotionDtos(String text);
}
