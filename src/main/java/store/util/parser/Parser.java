package store.util.parser;

import store.stock.dto.ItemDto;
import store.stock.dto.PromotionDto;

import java.util.List;

public interface Parser extends ItemParser, PromotionParser {
    ItemDto parseToItemDto(String text);
    List<ItemDto> parseToItemDtos(String text);
    String parseItemDtosToText(List<ItemDto> itemDtos);

    PromotionDto parseToPromotionDto(String text);
    List<PromotionDto> parseToPromotionDtos(String text);
}
