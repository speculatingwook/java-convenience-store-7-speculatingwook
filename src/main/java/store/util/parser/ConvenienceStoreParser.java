package store.util.parser;

import store.stock.Item;
import store.stock.dto.ItemDto;
import store.stock.dto.PromotionDto;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ConvenienceStoreParser implements Parser {

    @Override
    public List<ItemDto> parseToItemDtos(String data) {
        List<ItemDto> items = new ArrayList<>();
        List<String> lines = Arrays.stream(data.split("\n")).collect(Collectors.toList());
        lines.removeFirst();
        for (String line : lines) {
            items.add(parseToItemDto(line));
        }
        return items;
    }

    @Override
    public List<PromotionDto> parseToPromotionDtos(String data) {
        List<PromotionDto> promotions = new ArrayList<>();
        List<String> lines = Arrays.stream(data.split("\n")).collect(Collectors.toList());
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

    @Override
    public String parseStockToText(Map<Item, Integer> items) {
        String header = "name,price,quantity,promotion";
        return header + "\n" + parseItemDtos(items);
    }

    private String parseItemDtos(Map<Item, Integer> items) {
        StringBuilder itemsText = new StringBuilder();
        for (Item item : items.keySet()) {
            itemsText.append(addItemText(items, item));
        }
        return itemsText.toString();
    }

    private StringBuilder addItemText(Map<Item, Integer> items, Item item) {
        StringBuilder itemText = new StringBuilder();
        String promotion = item.getPromotionName();
        if (promotion.isEmpty()){
            promotion = "null";
        }
        itemText.append(String.join(",", item.getName(), String.valueOf(item.getPrice()), String.valueOf(items.get(item)), promotion)).append("\n");
        return itemText;
    }

    public static String formatNumberWithComma(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }

}
