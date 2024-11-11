package store.stock;

import store.stock.dto.ItemDto;
import store.stock.dto.PromotionDto;
import store.io.reader.Reader;
import store.stock.item.Item;
import store.stock.item.Items;
import store.stock.item.Promotion;
import store.stock.item.Promotions;
import store.util.parser.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryFactory {
    private final Reader reader;
    private final Parser parser;

    public InventoryFactory(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    public Inventory createInventory(String productFileName, String promotionFileName) {
        String itemData = reader.read(productFileName);
        List<ItemDto> itemDtos = parser.parseToItemDtos(itemData);

        Promotions promotions = createPromotions(promotionFileName);
        HashMap<Item, Integer> inventory = createItems(itemDtos, promotions);

        return new Inventory(new Items(inventory));
    }

    private HashMap<Item, Integer> createItems(List<ItemDto> itemDtos, Promotions promotions) {
        HashMap<Item, Integer> inventory = new HashMap<>();
        for (ItemDto itemDto : itemDtos) {
            Item item = new Item(itemDto.name(), itemDto.price(), promotions.getPromotion(itemDto.promotion()));
            inventory.put(item, Integer.parseInt(itemDto.quantity()));
        }
        inventory.putAll(getEmptyItems(inventory));
        return inventory;
    }

    private HashMap<Item, Integer> getEmptyItems(HashMap<Item, Integer> items) {
        HashMap<Item, Integer> emptyItems = new HashMap<>();
        for (Item item : items.keySet()) {
            if (item.isPromotionPresent() && isItemWithoutPromotionNotExist(items, item.getName())) {
                Item emptyItem = new Item(item.getName(), String.valueOf(item.getPrice()));
                emptyItems.put(emptyItem, 0);
            }
        }
        return emptyItems;
    }

    private Promotions createPromotions(String fileName) {
        String promotionData = reader.read(fileName);
        List<PromotionDto> promotionDtos = parser.parseToPromotionDtos(promotionData);
        return new Promotions(
                promotionDtos.stream()
                        .map(this::createPromotion)
                        .collect(Collectors.toList()));
    }

    private Promotion createPromotion(PromotionDto promotionDto) {
        return new Promotion(
                promotionDto.name(),
                promotionDto.buy(),
                promotionDto.get(),
                promotionDto.startDate(),
                promotionDto.endDate());
    }

    private boolean isItemWithoutPromotionNotExist(Map<Item, Integer> items, String itemName) {
        List<Item> emptyItems = items
                .keySet()
                .stream()
                .filter(item -> item.getName().equals(itemName)).filter(item -> !item.isPromotionPresent()).toList();
        return emptyItems.isEmpty();
    }
}
