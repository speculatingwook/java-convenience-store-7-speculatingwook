package store.service.parser;

public interface Parser<T> {
    T parse(String text);
}
