package org.example;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Утилітарний клас для аналізу списку транзакцій.
 * <p>
 * Клас абстрактний, оскільки не призначений для створення екземплярів:
 * усі методи статичні, а дані для аналізу передаються параметром.
 */
public abstract class TransactionAnalyzer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");

    // Метод для розрахунку загального балансу
    public static double calculateTotalBalance(List<Transaction> transactions) {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    // Метод для підрахунку транзакцій за конкретний місяць і рік
    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate(), DATE_FORMATTER);
            String transactionMonthYear = date.format(MONTH_YEAR_FORMATTER);
            if (transactionMonthYear.equals(monthYear)) {
                count++;
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0) // Вибірка лише витрат (від'ємні значення)
                .sorted(Comparator.comparing(Transaction::getAmount)) // Сортування за сумою
                .limit(10) // Обмеження результату першими 10 записами
                .collect(Collectors.toList()); // Збір результату в список
    }

    // 1. Метод для пошуку НАЙБІЛЬШОЇ витрати за вказаний місяць
    public static Transaction getLargestExpenseByMonth(List<Transaction> transactions, String monthYear) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0) // Відфільтровуємо лише витрати
                .filter(t -> {
                    LocalDate date = LocalDate.parse(t.getDate(), DATE_FORMATTER);
                    return date.format(MONTH_YEAR_FORMATTER).equals(monthYear);
                })
                // Шукаємо найменше число (наприклад, -3000 менше ніж -100, тому це найбільша витрата)
                .min(Comparator.comparing(Transaction::getAmount))
                .orElse(null); // Якщо нічого не знайдено, повертаємо null
    }

    // 2. Метод для пошуку НАЙМЕНШОЇ витрати за вказаний місяць
    public static Transaction getSmallestExpenseByMonth(List<Transaction> transactions, String monthYear) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = LocalDate.parse(t.getDate(), DATE_FORMATTER);
                    return date.format(MONTH_YEAR_FORMATTER).equals(monthYear);
                })
                // Шукаємо найбільше число (ближче до нуля)
                .max(Comparator.comparing(Transaction::getAmount))
                .orElse(null);
    }

    // 3. Метод для підрахунку сумарних витрат по кожній КАТЕГОРІЇ
    public static Map<String, Double> getExpensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        Transaction::getDescription, // Групуємо за назвою категорії
                        Collectors.summingDouble(t -> Math.abs(t.getAmount())) // Додаємо суми, перетворюючи їх на додатні числа (по модулю)
                ));
    }
}
