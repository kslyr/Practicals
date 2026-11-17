package org.example.processing;

import org.example.model.Order;
import org.example.model.Product;
import org.example.model.exception.UnsupportedProductTypeException;
import org.example.processing.internal.OrderLogger;
import org.example.storage.OrderStorage;

import java.util.Objects;
import java.util.function.ToDoubleFunction;

/** Обробник замовлень, обмежений типом товару: T extends Product. */
public class OrderProcessor<T extends Product> {
    private final Class<T> productType;
    private final OrderStorage storage;
    private final ToDoubleFunction<Order<T>> totalCalculator;

    public OrderProcessor(Class<T> productType,
                          OrderStorage storage,
                          ToDoubleFunction<Order<T>> totalCalculator) {
        this.productType = Objects.requireNonNull(productType);
        this.storage = Objects.requireNonNull(storage);
        this.totalCalculator = Objects.requireNonNull(totalCalculator);
    }

    public void process(Order<? extends Product> order) {
        Product product = order.getProduct();
        // T стирається під час компіляції, тому перевіряємо тип через Class<T>
        if (!productType.isInstance(product)) {
            throw new UnsupportedProductTypeException(productType, product.getClass());
        }
        Order<T> typedOrder = new Order<>(order.getId(), productType.cast(product), order.getQuantity());

        double total = totalCalculator.applyAsDouble(typedOrder);
        storage.save(typedOrder);
        storage.addRevenue(total);
        OrderLogger.logProcessed(typedOrder, total);
    }
}
