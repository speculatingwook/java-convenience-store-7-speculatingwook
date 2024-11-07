package store.service.parser;


import store.dto.PromotionDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PromotionParser implements Parser<PromotionDto> {
    private final List<PromotionDto> promotionDtos;

    public PromotionParser(String data) {
        this.promotionDtos = parsePromotions(data);
    }

    private List<PromotionDto> parsePromotions(String data) {
        List<PromotionDto> promotions = new ArrayList<>();
        List<String> lines = Arrays.asList(data.split("\n"));
        lines.removeFirst();
        for (String line : lines) {
            promotions.add(parse(line));
        }
        return promotions;
    }

    public List<PromotionDto> getPromotionDtos() {
        return List.copyOf(promotionDtos);
    }


    @Override
    public PromotionDto parse(String data) {
        List<String> split = Arrays.stream(data.split(",")).toList();
        return new PromotionDto(split.get(0), split.get(1), split.get(2), split.get(3), split.get(4));
    }
}
