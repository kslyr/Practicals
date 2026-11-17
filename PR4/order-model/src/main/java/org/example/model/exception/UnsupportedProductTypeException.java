package org.example.model.exception;

/**
 * Викидається, коли обробник, розрахований на один тип товару,
 * отримує замовлення з товаром іншого типу (type erasure не дає зловити це під час компіляції).
 */
public class UnsupportedProductTypeException extends RuntimeException {
    public UnsupportedProductTypeException(Class<?> expected, Class<?> actual) {
        super("Обробник розрахований на " + expected.getSimpleName()
                + ", а отримав " + actual.getSimpleName());
    }
}
