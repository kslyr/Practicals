package org.example.app;

import org.example.model.Clothing;
import org.example.model.Electronics;
import org.example.model.Order;
import org.example.model.Product;
import org.example.processing.OrderProcessor;
import org.example.storage.OrderStorage;

import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        OrderStorage storage = new OrderStorage();

        // Лямбди задають правило розрахунку суми для кожного типу товару
        OrderProcessor<Electronics> electronicsProcessor = new OrderProcessor<>(
                Electronics.class, storage,
                order -> order.getProduct().getPrice() * order.getQuantity());

        OrderProcessor<Clothing> clothingProcessor = new OrderProcessor<>(
                Clothing.class, storage,
                order -> {
                    double sum = order.getProduct().getPrice() * order.getQuantity();
                    return order.getQuantity() >= 3 ? sum * 0.9 : sum; // знижка 10% від 3 штук
                });

        // Посилання на методи: ProductFactory::randomElectronics, ProductFactory::randomClothing
        List<Order<Electronics>> electronicsOrders = Stream.generate(ProductFactory::randomElectronics)
                .limit(5)
                .map(product -> ProductFactory.orderFor(product, storage))
                .toList();
        List<Order<Clothing>> clothingOrders = Stream.generate(ProductFactory::randomClothing)
                .limit(5)
                .map(product -> ProductFactory.orderFor(product, storage))
                .toList();

        try (OrderDispatcher dispatcher = new OrderDispatcher(4)) {
            electronicsOrders.forEach(order -> dispatcher.dispatch(electronicsProcessor, order));
            clothingOrders.forEach(order -> dispatcher.dispatch(clothingProcessor, order));

            // Навмисна помилка: замовлення на одяг потрапляє в обробник електроніки
            Order<? extends Product> wrongOrder = clothingOrders.get(0);
            dispatcher.dispatch(electronicsProcessor, wrongOrder);
        } // close() чекає завершення всіх задач

        System.out.println("\n--- Електроніка в сховищі ---");
        storage.findByType(Electronics.class).forEach(System.out::println);

        System.out.println("\n--- Замовлення від 3 штук ---");
        storage.findBy(order -> order.getQuantity() >= 3).forEach(System.out::println);

        System.out.printf("%nЗагальна виручка: %.2f%n", storage.getRevenue());
    }
}
