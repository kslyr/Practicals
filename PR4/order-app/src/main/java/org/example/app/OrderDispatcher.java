package org.example.app;

import org.example.model.Order;
import org.example.model.Product;
import org.example.model.exception.UnsupportedProductTypeException;
import org.example.processing.OrderProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/** Роздає замовлення по потоках із пулу. */
class OrderDispatcher implements AutoCloseable {
    private final ExecutorService executor;

    OrderDispatcher(int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
    }

    Future<?> dispatch(OrderProcessor<?> processor, Order<? extends Product> order) {
        return executor.submit(() -> {
            try {
                processor.process(order);
            } catch (UnsupportedProductTypeException e) {
                System.err.printf("[%s] Помилка замовлення #%d: %s%n",
                        Thread.currentThread().getName(), order.getId(), e.getMessage());
            }
        });
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
