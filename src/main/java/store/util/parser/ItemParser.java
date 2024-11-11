package store.util.parser;

import store.stock.item.Item;
import store.stock.dto.ItemDto;
import store.stock.item.Items;

import java.util.List;
import java.util.Map;

public interface ItemParser {
    ItemDto parseToItemDto(String text);
    List<ItemDto> parseToItemDtos(String text);
    String parseStockToText(Items items);
}