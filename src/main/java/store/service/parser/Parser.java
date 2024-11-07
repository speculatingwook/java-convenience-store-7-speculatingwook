package store.service.parser;

import store.dto.ItemDto;
import store.dto.PromotionDto;

import java.util.List;

public interface Parser extends ItemParser, PromotionParser {
    ItemDto parseToItemDto(String text);
    List<ItemDto> parseToItemDtos(String text);

    PromotionDto parseToPromotionDto(String text);
    List<PromotionDto> parseToPromotionDtos(String text);
}
