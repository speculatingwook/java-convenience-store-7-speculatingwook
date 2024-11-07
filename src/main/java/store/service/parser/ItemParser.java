package store.service.parser;

import store.dto.ItemDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemParser implements Parser<ItemDto> {
    private final List<ItemDto> itemDtos;

    public ItemParser(String data) {
        this.itemDtos = parseItems(data);
    }

    private List<ItemDto> parseItems(String data) {
        List<ItemDto> items = new ArrayList<>();
        List<String> lines = Arrays.asList(data.split("\n"));
        lines.removeFirst();
        for (String line : lines) {
            items.add(parse(line));
        }
        return items;
    }

    private List<ItemDto> getItemDtos() {
        return List.copyOf(itemDtos);
    }

    @Override
    public ItemDto parse(String data) {
        List<String> split = Arrays.stream(data.split(",")).toList();
        return new ItemDto(split.get(0), split.get(1), split.get(2), split.get(3));
    }
}
