package org.example;

import java.util.ArrayList;
import java.util.List;


public class TransactionParser {
    public List<Transaction> parse(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();

        for (String line : lines) {
            // Пропускаємо порожні рядки та шапку таблиці
            if (line.trim().isEmpty() || line.contains("Дата")) {
                continue;
            }

            // Розділяємо рядок по комі
            String[] values = line.split(",");
            if (values.length >= 3) {
                String date = values[0].trim();
                double amount = Double.parseDouble(values[1].trim());
                String description = values[2].trim();

                // Створюємо та додаємо новий об'єкт транзакції
                transactions.add(new Transaction(date, amount, description));
            }
        }

        return transactions;
    }
}