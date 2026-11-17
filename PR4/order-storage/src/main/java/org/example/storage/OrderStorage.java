package org.example.storage;

import org.example.model.Order;
import org.example.model.Product;
import org.example.storage.internal.IdGenerator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.function.Predicate;

/** Потокобезпечне сховище замовлень. */
public class OrderStorage {
    private final Map<Long, Order<? extends Product>> orders = new ConcurrentSkipListMap<>();
    private final DoubleAdder revenue = new DoubleAdder();

    /** Єдиний спосіб отримати id ззовні модуля: сам IdGenerator прихований. */
    public long nextOrderId() {

        return IdGenerator.next();
    }

    public void save(Order<? extends Product> order) {

        orders.put(order.getId(), order);
    }

    public Optional<Order<? extends Product>> findById(long id) {

        return Optional.ofNullable(orders.get(id));
    }

    public List<Order<? extends Product>> findBy(Predicate<Order<? extends Product>> condition) {
        return orders.values().stream().filter(condition).toList();
    }

    @SuppressWarnings("unchecked")
    public <T extends Product> List<Order<T>> findByType(Class<T> type) {
        return orders.values().stream()
                .filter(order -> type.isInstance(order.getProduct()))
                .map(order -> (Order<T>) order)   // безпечно: тип уже перевірено у filter
                .toList();
    }

    public void addRevenue(double amount) {
        revenue.add(amount);
    }

    public double getRevenue() {
        return revenue.sum();
    }
}
