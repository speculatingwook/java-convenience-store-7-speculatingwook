package store.service.parser;

import store.domain.Promotion;
import store.domain.Item;
import store.dto.ItemDto;
import store.dto.PromotionDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvenienceStoreParser implements Parser {
    private final List<ItemDto> itemDtos;
    private final List<PromotionDto> promotionDtos;

    public ConvenienceStoreParser(String itemData, String promotionData) {
        this.itemDtos = parseToItemDtos(itemData);
        this.promotionDtos = parseToPromotionDtos(promotionData);
    }

    public List<ItemDto> getItemDtos() {
        return List.copyOf(itemDtos);
    }

    public List<PromotionDto> getPromotionDtos() {
        return List.copyOf(promotionDtos);
    }

    @Override
    public List<ItemDto> parseToItemDtos(String data) {
        List<ItemDto> items = new ArrayList<>();
        List<String> lines = Arrays.asList(data.split("\n"));
        lines.removeFirst();
        for (String line : lines) {
            items.add(parseToItemDto(line));
        }
        return items;
    }

    @Override
    public List<PromotionDto> parseToPromotionDtos(String data) {
        List<PromotionDto> promotions = new ArrayList<>();
        List<String> lines = Arrays.asList(data.split("\n"));
        lines.removeFirst();
        for (String line : lines) {
            promotions.add(parseToPromotionDto(line));
        }
        return promotions;
    }

    @Override
    public PromotionDto parseToPromotionDto(String data) {
        List<String> split = Arrays.stream(data.split(",")).toList();
        return new PromotionDto(split.get(0), split.get(1), split.get(2), split.get(3), split.get(4));
    }

    @Override
    public ItemDto parseToItemDto(String data) {
        List<String> split = Arrays.stream(data.split(",")).toList();
        return new ItemDto(split.get(0), split.get(1), split.get(2), split.get(3));
    }
}