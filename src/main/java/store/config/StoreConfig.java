package store.config;

import store.io.StoreInput;
import store.io.StoreView;
import store.io.YesNoOption;
import store.io.reader.FileReader;
import store.io.reader.Reader;
import store.parser.ConvenienceStoreParser;
import store.parser.Parser;
import store.payment.discount.ConvenienceStoreDiscount;
import store.payment.discount.Discount;
import store.pos.Cart;
import store.pos.PosMachine;
import store.pos.PosScanner;
import store.stock.Inventory;
import store.stock.InventoryFactory;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class StoreConfig {
    private static final Integer MEMBERSHIP_DISCOUNT_RATE = 30;

    public void registerIO() {
        Container.register(StoreView.class, StoreView::new);
        Container.register(StoreInput.class, () -> new StoreInput(Container.getInstance(StoreView.class)));
        Container.register(YesNoOption.class, () -> new YesNoOption(Container.getInstance(StoreInput.class)));
        Container.register(Reader.class, FileReader::new);
    }

    public void registerStockServices() {
        Container.register(InventoryFactory.class, () ->
                new InventoryFactory(Container.getInstance(Reader.class), Container.getInstance(Parser.class))
        );
        Container.register(Inventory.class, () -> {
            InventoryFactory inventoryFactory = Container.getInstance(InventoryFactory.class);
            return inventoryFactory.createInventory();
        });
    }

    public void registerParser() {
        Container.register(Parser.class, ConvenienceStoreParser::new);
    }

    public void registerPosServices() {
        Inventory inventory = Container.getInstance(Inventory.class);
        StoreView view = Container.getInstance(StoreView.class);
        StoreInput input = Container.getInstance(StoreInput.class);

        Container.register(PosScanner.class, ()-> new PosScanner(inventory));
        Container.register(PosMachine.class, retryOnFail(()-> tryCreatePosMachine(inventory, view, input)));
    }

    public void registerDiscountServices() {
        Container.register(Discount.class, () ->
                new ConvenienceStoreDiscount(MEMBERSHIP_DISCOUNT_RATE, Container.getInstance(Inventory.class))
        );
    }

    private PosMachine tryCreatePosMachine(Inventory inventory, StoreView view, StoreInput input) {
        view.printCurrentStock(inventory.toString());
        String itemRequest = input.readItemRequest();
        Cart cart = new Cart(itemRequest);
        return new PosMachine(inventory, Container.getInstance(PosScanner.class), cart);
    }


    /**
     * 특정 작업이 실패할 경우 재시도하는 기능을 제공하는 메서드.
     * 주어진 Supplier가 예외를 발생시킬 경우, 계속해서 재시도한다.
     * 실패 시 사용자가 에러 메시지를 볼 수 있도록 처리하고,
     * 특정 예외(NoSuchElementException) 발생 시에는 그대로 예외를 던진다.
     *
     * @param supplier 실행할 작업을 제공하는 Supplier
     * @param <T>      Supplier가 반환하는 객체의 타입
     * @return 재시도 로직이 포함된 새로운 Supplier
     */
    private static <T> Supplier<T> retryOnFail(Supplier<T> supplier) {
        return () -> executeWithRetry(supplier);
    }

    /**
     * Supplier의 작업을 실행하고 필요한 경우 재시도하는 내부 메서드.
     * RuntimeException 발생 시 에러 메시지를 표시하고 재시도하며,
     * NoSuchElementException은 상위로 전파한다.
     *
     * @param supplier 실행할 작업을 제공하는 Supplier
     * @param <T>      반환될 객체의 타입
     * @return supplier가 성공적으로 제공한 객체
     * @throws NoSuchElementException supplier가 해당 예외를 발생시킬 경우
     */
    private static <T> T executeWithRetry(Supplier<T> supplier) {
        while (true) try {
            return supplier.get();
        } catch (RuntimeException e) {
            if (e instanceof NoSuchElementException) throw e;
            StoreView.showError(e.getMessage());
        }
    }

}