package store;

import store.config.Container;
import store.config.StoreConfig;
import store.io.StoreInput;
import store.io.StoreView;
import store.io.YesNoOption;
import store.payment.Payment;
import store.payment.Receipt;
import store.payment.discount.Discount;
import store.pos.Cart;
import store.pos.OrderItemInfo;
import store.pos.PosMachine;
import store.pos.PosScanner;
import store.stock.Inventory;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class Application {
    public static void main(String[] args) {
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.registerCoreServices();

        StoreInput input = Container.getInstance(StoreInput.class);
        StoreView view = Container.getInstance(StoreView.class);
        Inventory inventory = Container.getInstance(Inventory.class);
        PosScanner posScanner = Container.getInstance(PosScanner.class);
        YesNoOption option = Container.getInstance(YesNoOption.class);

        Container.register(PosMachine.class, retryOnFail(() -> {
            try {
                view.printCurrentStock(inventory.toString());
                String itemRequest = input.readItemRequest();
                Cart cart = new Cart(itemRequest);
                PosMachine machine = new PosMachine(inventory, posScanner, cart);
                return machine;
            } catch (Exception e) {
                throw e;
            }
        }));

        PosMachine posMachine = Container.getInstance(PosMachine.class);
        posMachine.scanCartItems();
        posMachine.updateOrderItemInfo(option);
        OrderItemInfo orderItemInfo = posMachine.getOrderItemInfo();

        Container.register(Payment.class, ()-> {
            Receipt receipt = new Receipt();
            return new Payment(receipt, Container.getInstance(Discount.class), orderItemInfo);
        });

        Payment payment = Container.getInstance(Payment.class);
        view.printReceipt(payment.issueReceipt(option));
        Container.reset();
    }


    /**
     * 특정 작업이 실패할 경우 재시도하는 기능을 제공하는 메서드.
     * 주어진 Supplier가 예외를 발생시킬 경우, 계속해서 재시도한다.
     * 실패 시 사용자가 에러 메시지를 볼 수 있도록 처리하고,
     * 특정 예외(NoSuchElementException) 발생 시에는 그대로 예외를 던진다.
     *
     * @param supplier 실행할 작업을 제공하는 Supplier
     * @param <T>      Supplier가 반환하는 객체의 타입
     * @return Supplier가 제공하는 객체
     */
    private static <T> Supplier<T> retryOnFail(Supplier<T> supplier) {
        return () -> {
            while (true) {
                try {
                    return supplier.get();
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException(e.getMessage());
                } catch (RuntimeException e) {
                    StoreView.showError(e.getMessage());
                }
            }
        };
    }
}
