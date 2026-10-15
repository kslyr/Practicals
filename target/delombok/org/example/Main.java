package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Створення користувача
        User user = new User("Студент");


        // Створення категорій
        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");
        Category computer = new Category(4, "Комп'ютер");

        // Створення товарів
        Product product1 = new Product(1, "Ноутбук", new BigDecimal("19999.99"), "Високопродуктивний ноутбук для роботи та ігор", electronics);
        Product product2 = new Product(2, "Смартфон", new BigDecimal("12999.50"), "Смартфон з великим екраном та високою автономністю", smartphones);
        Product product3 = new Product(3, "Навушники", new BigDecimal("2499.00"), "Бездротові навушники з шумозаглушенням", accessories);
        Product product4 = new Product(4, "Клавіатура", new BigDecimal("2000.00"), "Магнітна клавіатура", computer);

        // Каталог товарів
        List<Product> catalog = new ArrayList<>();
        catalog.add(product1);
        catalog.add(product2);
        catalog.add(product3);
        catalog.add(product4);

        while (true) {
            System.out.println("\nВиберіть опцію:");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Видалити товар з кошика");
            System.out.println("4 - Переглянути кошик");
            System.out.println("5 - Пошук товарів");
            System.out.println("6 - Зробити замовлення");
            System.out.println("7 - Переглянути історію замовлень");
            System.out.println("0 - Вийти");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Очищення буфера після nextInt()

            switch (choice) {
                case 1:
                    for (Product p : catalog) {
                        System.out.println(p);
                    }
                    break;

                case 2:
                    System.out.println("Введіть ID товару для додавання до кошика:");
                    int addId = scanner.nextInt();
                    Product toAdd = findById(catalog, addId);
                    if (toAdd != null) {
                        user.getCart().addProduct(toAdd);
                        System.out.println("Товар додано до кошика!");
                    } else {
                        System.out.println("Товар з таким ID не знайдено.");
                    }
                    break;

                case 3:
                    System.out.println("Введіть ID товару для видалення з кошика:");
                    int removeId = scanner.nextInt();
                    Product toRemove = findById(user.getCart().getProducts(), removeId);
                    if (toRemove != null) {
                        user.getCart().removeProduct(toRemove);
                        System.out.println("Товар видалено з кошика.");
                    } else {
                        System.out.println("Такого товару немає в кошику.");
                    }
                    break;

                case 4:
                    System.out.println(user.getCart());
                    break;

                case 5:
                    System.out.println("Шукати за: 1 - назвою, 2 - категорією");
                    int searchChoice = scanner.nextInt();
                    scanner.nextLine();
                    if (searchChoice == 1) {
                        System.out.println("Введіть назву або її частину:");
                        String query = scanner.nextLine();
                        printSearchResults(searchByName(catalog, query));
                    } else if (searchChoice == 2) {
                        System.out.println("Введіть назву категорії:");
                        String catQuery = scanner.nextLine();
                        printSearchResults(searchByCategory(catalog, catQuery));
                    } else {
                        System.out.println("Невірний вибір.");
                    }
                    break;

                case 6:
                    if (user.getCart().getProducts().isEmpty()) {
                        System.out.println("Кошик порожній. Додайте товари перед оформленням замовлення.");
                    } else {
                        Order order = new Order(user.getCart());
                        user.addOrderToHistory(order); // Збереження замовлення в історію
                        System.out.println("Замовлення оформлено та збережено в історію:");
                        System.out.println(order);
                        user.getCart().clear();
                    }
                    break;

                case 7:
                    if (user.getOrderHistory().isEmpty()) {
                        System.out.println("Історія замовлень порожня.");
                    } else {
                        System.out.println("--- ІСТОРІЯ ЗАМОВЛЕНЬ ---");
                        for (Order o : user.getOrderHistory()) {
                            System.out.println(o);
                            System.out.println("-------------------------");
                        }
                    }
                    break;

                case 0:
                    System.out.println("Дякуємо, що використовували наш магазин!");
                    return;

                default:
                    System.out.println("Невідома опція. Спробуйте ще раз.");
                    break;
            }
        }
    }

    // Допоміжний метод пошуку за ID
    private static Product findById(List<Product> products, int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // Пошук товарів за назвою
    public static List<Product> searchByName(List<Product> catalog, String query) {
        List<Product> result = new ArrayList<>();
        for (Product p : catalog) {
            if (p.getName().toLowerCase().contains(query.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    // Пошук товарів за категорією
    public static List<Product> searchByCategory(List<Product> catalog, String categoryName) {
        List<Product> result = new ArrayList<>();
        for (Product p : catalog) {
            if (p.getCategory().getName().equalsIgnoreCase(categoryName)) {
                result.add(p);
            }
        }
        return result;
    }

    // Друк результатів пошуку
    private static void printSearchResults(List<Product> results) {
        if (results.isEmpty()) {
            System.out.println("Нічого не знайдено.");
        } else {
            System.out.println("Знайдені товари:");
            for (Product p : results) {
                System.out.println(p);
            }
        }
    }
}