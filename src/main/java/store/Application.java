package store;

import store.domain.ConvenienceStore;
import store.domain.Inventory;
import store.domain.Receipt;
import store.io.StoreInput;
import store.io.StoreView;
import store.io.reader.FileReader;
import store.io.reader.Reader;
import store.io.writer.FileWriter;
import store.io.writer.Writer;
import store.service.InventoryFactory;
import store.service.discount.ConvenienceStoreDiscount;
import store.service.discount.Discount;
import store.service.parser.ConvenienceStoreParser;
import store.service.parser.Parser;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        Reader fileReader = new FileReader();
        Writer fileWriter = new FileWriter();
        Receipt receipt = new Receipt();
        StoreView view = new StoreView();
        StoreInput input = new StoreInput(view);
        Parser convenienceStoreParser = new ConvenienceStoreParser(fileReader.read("products.md"), fileReader.read("promotions.md"));

        InventoryFactory inventoryFactory = new InventoryFactory(fileReader, convenienceStoreParser);
        Inventory inventory = inventoryFactory.createInventory();
        Discount discount = new ConvenienceStoreDiscount(30, inventory);

        ConvenienceStore convenienceStore = new ConvenienceStore(inventory, receipt, discount, fileWriter, convenienceStoreParser, input);
        view.greet();
        view.printCurrentStock(inventory.toString());
        String itemRequest = input.readItemRequest();
        Map<String, Integer> parsedInput = ConvenienceStoreParser.parseRequestToMap(itemRequest);
        convenienceStore.scan(parsedInput);
        convenienceStore.addItemsToCart();
        convenienceStore.issueReceipt();

        view.printReceipt(receipt.toString());
    }
}
