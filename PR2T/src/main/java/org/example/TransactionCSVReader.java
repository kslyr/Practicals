package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилітарний клас для читання транзакцій з CSV-файлу.
 * <p>
 * Клас абстрактний, оскільки не призначений для створення екземплярів:
 * уся логіка реалізована через статичний метод {@link #readTransactions(String)}.
 */
public abstract class TransactionCSVReader {

    /**
     * Зчитує транзакції з локального CSV-файлу.
     *
     * @param filePath шлях до CSV-файлу
     * @return список зчитаних транзакцій (порожній, якщо файл недоступний)
     */
    public static List<Transaction> readTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        Path path = Paths.get(filePath);
        // Читання локального файлу замість URL
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                // Пропускаємо заголовок: Дата,Сума,Категорія
                if (firstLine) {
                    firstLine = false;
                    if (!line.chars().anyMatch(Character::isDigit)) {
                        continue;
                    }
                }
                String[] values = line.split(",", 3);
                if (values.length < 3) {
                    continue;
                }
                try {
                    Transaction transaction = new Transaction(values[0], Double.parseDouble(values[1]), values[2]);
                    transactions.add(transaction);
                } catch (NumberFormatException e) {
                    System.err.println("Пропущено рядок з некоректною сумою: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
