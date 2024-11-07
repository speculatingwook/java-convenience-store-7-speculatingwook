package store.service.parser;

import store.dto.ItemDto;

import java.util.List;

public interface ItemParser {
    ItemDto parseToItemDto(String text);
    List<ItemDto> parseToItemDtos(String text);
}