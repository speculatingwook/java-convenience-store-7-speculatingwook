package store.payment;

import store.pos.PromotionCatalog;
import store.stock.Item;
import store.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Payment {
    private final Receipt receipt;
    private PromotionCatalog catalog;

    public Payment(Receipt receipt) {
        this.receipt = receipt;
    }

    public void receiveData(PromotionCatalog catalog) {
        this.catalog = catalog;
    }

    private Integer getTotalAmount() {
        int totalAmount = 0;
        for (Map.Entry<String, Integer> entry : requestItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            totalAmount += inventory.getItem(itemName).getPrice() * quantity;
        }
        return totalAmount;
    }

    private Integer getTotalCount() {
        int totalCount = 0;
        for (Map.Entry<String, Integer> entry : requestItems.entrySet()) {
            Integer quantity = entry.getValue();
            totalCount += quantity;
        }
        return totalCount;
    }
    // 이것도 결제
    public String issueReceipt() {
        int totalAmount = getTotalAmount();
        int totalCount = getTotalCount();
        int promotionDiscountAmount = receivePromotionDiscount();
        double membershipDiscountAmount = receiveMembershipDiscount();
        String membership = input.readMembershipDiscountRequest();
        if(membership.equals("Y")){
            receipt.addTotalDiscount(receivePromotionDiscount(), receiveMembershipDiscount());
            double totalPay = totalAmount - promotionDiscountAmount - membershipDiscountAmount;
            receipt.addResult(totalPay);
        }
        if(membership.equals("N")){
            receipt.addTotalDiscount(receivePromotionDiscount(), 0);
            double totalPay = totalAmount - promotionDiscountAmount;
            receipt.addResult(totalPay);
        }
        receipt.addTotalPrice(getTotalCount(), totalCount);
        writeChangedContent();
        return receipt.issueReceipt("W");
    }

    // 이것도 결제
    private void writeChangedContent() {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : requestItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            if(inventory.isItemInPromotion(itemName)) {
                Item item = inventory.getItemWithPromotion(itemName);
                itemDtos.add(new ItemDto(itemName, String.valueOf(item.getPrice()), String.valueOf(quantity),item.getPromotionName()));
            }
            if(!inventory.isItemInPromotion(itemName)) {
                Item item = inventory.getItemWithoutPromotion(itemName);
                itemDtos.add(new ItemDto(itemName, String.valueOf(item.getPrice()), String.valueOf(quantity),null));
            }
        }

        String content = parser.parseItemDtosToText(itemDtos);
        writer.write("products.md", content);
    }


    public Integer receivePromotionDiscount() {
        int totalDiscount = 0;
        for (Map.Entry<String, Integer> entry : promotionItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            totalDiscount += inventory.getItem(itemName).getPrice() * quantity;
        }
        return totalDiscount;
    }


    public Double receiveMembershipDiscount() {
        int totalPrice = 0;
        for (Map.Entry<String, Integer> entry : nonPromotionItems.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            totalPrice += inventory.getItem(itemName).getPrice() * quantity;
        }
        return discount.calculateMembershipDiscount(totalPrice);
    }
}
