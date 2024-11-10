package store;

import store.config.Container;
import store.config.StoreConfig;
import store.io.StoreView;
import store.io.YesNoOption;
import store.payment.Payment;
import store.pos.OrderItemInfo;
import store.pos.PosMachine;

public class Application {
    public static void main(String[] args) {
        processTransaction();
    }

    private static void processTransaction() {
        setup();
        StoreComponents components = initializeComponents();
        OrderItemInfo orderInfo = processOrder(components.posMachine, components.option);
        String receipt = handlePayment(components.payment, orderInfo, components.option);
        finalizeTransaction(components.view, receipt);
    }

    private static StoreComponents initializeComponents() {
        return new StoreComponents(
                Container.getInstance(StoreView.class),
                Container.getInstance(YesNoOption.class),
                Container.getInstance(PosMachine.class),
                Container.getInstance(Payment.class)
        );
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

    private static void finalizeTransaction(StoreView view, String receipt) {
        view.printReceipt(receipt);
        Container.reset();
    }

    private static void setup() {
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.registerIO();
        storeConfig.registerStockServices();
        storeConfig.registerParser();
        storeConfig.registerPosServices();
        storeConfig.registerPaymentServices();
    }

    private record StoreComponents(
            StoreView view,
            YesNoOption option,
            PosMachine posMachine,
            Payment payment
    ) {
    }
}
