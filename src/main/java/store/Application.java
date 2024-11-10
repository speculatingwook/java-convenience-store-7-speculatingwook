package store;

import store.config.Container;
import store.config.StoreConfig;
import store.io.StoreView;
import store.io.YesNoOption;
import store.payment.Payment;
import store.payment.Receipt;
import store.payment.discount.Discount;
import store.pos.OrderItemInfo;
import store.pos.PosMachine;

public class Application {
    public static void main(String[] args) {
        init();
        StoreView view = Container.getInstance(StoreView.class);
        YesNoOption option = Container.getInstance(YesNoOption.class);

        PosMachine posMachine = Container.getInstance(PosMachine.class);
        Payment payment = Container.getInstance(Payment.class);

        posMachine.scanCartItems();
        posMachine.updateOrderItemInfo(option);
        OrderItemInfo orderItemInfo = posMachine.getOrderItemInfo();

        payment.receiveOrderItemInfo(orderItemInfo);

        view.printReceipt(payment.issueReceipt(option));
        Container.reset();
    }

    private static void init() {
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.registerIO();
        storeConfig.registerStockServices();
        storeConfig.registerParser();
        storeConfig.registerPosServices();
        storeConfig.registerPaymentServices();
    }

}
