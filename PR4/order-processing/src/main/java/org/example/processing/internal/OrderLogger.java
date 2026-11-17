package org.example.processing.internal;

import org.example.model.Order;
import org.example.model.Product;

/** Службовий клас: форматує рядок логу. Ззовні модуля недоступний. */
public final class OrderLogger {
    private OrderLogger() {
    }

    public static void logProcessed(Order<? extends Product> order, double total) {
        System.out.printf("[%s] Замовлення #%d: %s x%d = %.2f%n",
                Thread.currentThread().getName(),
                order.getId(),
                order.getProduct().getName(),
                order.getQuantity(),
                total);
    }
}
