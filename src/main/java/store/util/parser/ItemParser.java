package store.util.parser;

import store.stock.dto.ItemDto;

import java.util.List;

public interface ItemParser {
    ItemDto parseToItemDto(String text);
    List<ItemDto> parseToItemDtos(String text);
    String parseItemDtosToText(List<ItemDto> itemDtos);
}