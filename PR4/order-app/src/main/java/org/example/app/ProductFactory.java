package org.example.app;

import com.github.javafaker.Faker;
import org.example.model.Clothing;
import org.example.model.Electronics;
import org.example.model.Order;
import org.example.model.Product;
import org.example.storage.OrderStorage;

/** Генерує тестові дані через JavaFaker. Faker не потокобезпечний, викликаємо лише з main-потоку. */
final class ProductFactory {
    private static final Faker FAKER = new Faker();

    private ProductFactory() {
    }

    static Electronics randomElectronics() {
        return Electronics.builder()
                .name(FAKER.commerce().productName())
                .price(FAKER.number().randomDouble(2, 50, 3000))
                .brand(FAKER.company().name())
                .warrantyMonths(FAKER.number().numberBetween(6, 37))
                .build();
    }

    static Clothing randomClothing() {
        return Clothing.builder()
                .name(FAKER.commerce().productName())
                .price(FAKER.number().randomDouble(2, 10, 200))
                .size(FAKER.options().option("XS", "S", "M", "L", "XL"))
                .material(FAKER.commerce().material())
                .build();
    }

    static <T extends Product> Order<T> orderFor(T product, OrderStorage storage) {
        return new Order<>(storage.nextOrderId(), product, FAKER.number().numberBetween(1, 6));
    }
}
