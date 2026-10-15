package org.example;

import java.util.List;
import java.util.Map;

/**
 * Утилітарний клас для виведення звітів у консоль.
 * <p>
 * Клас абстрактний, оскільки не призначений для створення екземплярів:
 * усі методи статичні й не зберігають жодного стану.
 */
public abstract class TransactionReportGenerator {

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    // Метод для виведення візуального звіту зірочками
    public static void printStarReport(Map<String, Double> categoryExpenses) {
        System.out.println("\n--- ВІЗУАЛЬНИЙ ЗВІТ ВИТРАТ ЗА КАТЕГОРІЯМИ ---");
        System.out.println("(* = 1000 грн)");

        for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();

            // Рахуємо кількість зірочок (ділимо суму на 1000)
            int starsCount = (int) (amount / 1000);

            // Створюємо рядок потрібної довжини із зірочок (мінімум 1 зірочка для будь-якої витрати)
            String stars = "*".repeat(Math.max(1, starsCount));

            // Виводимо вирівняний текст у консоль
            System.out.printf("%-20s : %s (%.2f)%n", category, stars, amount);
        }
    }

    // Метод для виведення найбільшої/найменшої витрати
    public static void printMinMaxExpenses(String monthYear, Transaction largest, Transaction smallest) {
        System.out.println("\n--- АНАЛІЗ ВИТРАТ ЗА " + monthYear + " ---");
        if (largest != null && smallest != null) {
            System.out.println("Найбільша витрата: " + largest.getDescription() + " (" + largest.getAmount() + ")");
            System.out.println("Найменша витрата : " + smallest.getDescription() + " (" + smallest.getAmount() + ")");
        } else {
            System.out.println("За вказаний період витрат не знайдено.");
        }
    }
}
