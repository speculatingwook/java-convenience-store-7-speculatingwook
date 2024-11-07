package store.service;

import store.domain.Inventory;
import store.domain.Item;
import store.domain.Promotion;
import store.domain.Promotions;
import store.dto.ItemDto;
import store.dto.PromotionDto;
import store.service.parser.Parser;
import store.service.reader.Reader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryFactory {
    private final Reader reader;
    private final Parser parser;
    
    public InventoryFactory(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    public Inventory createInventory() {
        String itemData = reader.read("products.md");
        List<ItemDto> itemDtos = parser.parseToItemDtos(itemData);

        Promotions promotions = createPromotions();
        HashMap<Item, Integer> inventory = createItems(itemDtos, promotions);

        return new Inventory(inventory);
    }

    private HashMap<Item, Integer> createItems(List<ItemDto> itemDtos, Promotions promotions) {
        HashMap<Item, Integer> inventory = new HashMap<>();
        for(ItemDto itemDto : itemDtos) {
            Item item = new Item(itemDto.name(), itemDto.price(), promotions.getPromotion(itemDto.promotion()));
            inventory.put(item, Integer.parseInt(itemDto.quantity()));
        }
        return inventory;
    }

    private Promotions createPromotions() {
        String promotionData = reader.read("promotions.md");
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
}
