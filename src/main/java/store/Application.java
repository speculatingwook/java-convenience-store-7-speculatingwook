package store;

import store.config.Container;
import store.config.StoreConfig;
import store.io.StoreView;
import store.io.YesNoOption;
import store.io.writer.Writer;
import store.payment.Payment;
import store.pos.OrderItemInfo;
import store.pos.PosMachine;
import store.stock.Inventory;
import store.util.parser.Parser;

import static store.config.StoreConfig.PRODUCT_FILE_NAME;

public class Application {
    private static void setup() {
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.registerIO();
        storeConfig.registerStockServices();
        storeConfig.registerParser();
        storeConfig.registerPosServices();
        storeConfig.registerPaymentServices();
    }

    private static StoreComponents initializeComponents() {
        return new StoreComponents(
                Container.getInstance(StoreView.class),
                Container.getInstance(YesNoOption.class),
                Container.getInstance(PosMachine.class),
                Container.getInstance(Payment.class),
                Container.getInstance(Inventory.class)
        );
    }

    public static void main(String[] args) {
        processTransaction();
    }

    private static void processTransaction() {
        setup();
        StoreComponents components = initializeComponents();
        OrderItemInfo orderInfo = processOrder(components.posMachine, components.option);
        String receipt = handlePayment(components.payment, orderInfo, components.option);
        finalizeTransaction(components, receipt, orderInfo);
    }

    private static OrderItemInfo processOrder(PosMachine posMachine, YesNoOption option) {
        posMachine.scanCartItems();
        posMachine.updateOrderItemInfo(option);
        return posMachine.getOrderItemInfo();
    }

    private static String handlePayment(Payment payment, OrderItemInfo orderInfo, YesNoOption option) {
        payment.receiveOrderItemInfo(orderInfo);
        return payment.issueReceipt(option);
    }

    private static void finalizeTransaction(StoreComponents components, String receipt, OrderItemInfo orderItemInfo) {
        components.view.printReceipt(receipt);
        saveInventoryState(components.inventory, orderItemInfo);
        Container.reset();

        if (components.option.buyMoreItems()) {
            processTransaction();
        }
    }

    private static void saveInventoryState(Inventory inventory, OrderItemInfo orderItemInfo) {
        inventory.deductItems(orderItemInfo.getOrderItems().items());
        inventory.refresh();
        Parser parser = Container.getInstance(Parser.class);
        Writer writer = Container.getInstance(Writer.class);
        writer.write(PRODUCT_FILE_NAME, parser.parseStockToText(inventory.getInventory()));
    }

    private record StoreComponents(
            StoreView view,
            YesNoOption option,
            PosMachine posMachine,
            Payment payment,
            Inventory inventory
    ) {
    }
}
