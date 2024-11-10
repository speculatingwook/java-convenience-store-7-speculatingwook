package store.util.parser;

import store.stock.Item;
import store.stock.dto.ItemDto;

import java.util.List;
import java.util.Map;

public interface ItemParser {
    ItemDto parseToItemDto(String text);
    List<ItemDto> parseToItemDtos(String text);
    String parseStockToText(Map<Item, Integer> items);
}