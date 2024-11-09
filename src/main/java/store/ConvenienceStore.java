package store;

import store.io.StoreInput;
import store.io.writer.Writer;
import store.pos.Cart;
import store.pos.PosScanner;
import store.parser.Parser;

public class ConvenienceStore {
    private final PosScanner posMachine;
    private final Cart cart;
    private final Writer writer;
    private final Parser parser;
    private final StoreInput input;

    public ConvenienceStore(PosScanner posMachine, Cart cart, Writer writer, Parser parser, StoreInput input) {
        this.posMachine = posMachine;
        this.cart = cart;
        this.writer = writer;
        this.parser = parser;
        this.input = input;
    }



}