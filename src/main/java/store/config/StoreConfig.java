package store.config;

import store.io.reader.FileReader;
import store.io.reader.Reader;
import store.parser.ConvenienceStoreParser;
import store.parser.Parser;
import store.payment.discount.ConvenienceStoreDiscount;
import store.payment.discount.Discount;
import store.stock.Inventory;
import store.stock.InventoryFactory;

public class StoreConfig {
    private static final Integer MEMBERSHIP_DISCOUT_RATE = 30;

    public void registerCoreServices() {
        Container.register(Parser.class, ConvenienceStoreParser::new);
        Container.register(Reader.class, FileReader::new);
        Container.register(InventoryFactory.class, ()->
                new InventoryFactory(Container.getInstance(Reader.class), Container.getInstance(Parser.class))
        );
        Container.register(Inventory.class, ()-> {
            InventoryFactory inventoryFactory = Container.getInstance(InventoryFactory.class);
            return inventoryFactory.createInventory();
        });
        Container.register(Discount.class, ()->
                new ConvenienceStoreDiscount(MEMBERSHIP_DISCOUT_RATE, Container.getInstance(Inventory.class))
        );
    }

}
