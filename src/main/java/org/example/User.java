package org.example;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class User {
    private String username;
    private Cart cart;
    private List<Order> orderHistory; // Тут буде зберігатися історія замовлень

    public User(String username) {
        this.username = username;
        this.cart = new Cart();
        this.orderHistory = new ArrayList<>(); // Ініціалізуємо порожню історію
    }

    // Метод для збереження замовлення в історію
    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
        System.out.println("Замовлення збережено в історію користувача: " + username);
    }
}