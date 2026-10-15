package org.example;

import java.util.Map;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "pr2.csv";

        DataReader reader = new DataReader();
        List<String> rawLines = reader.readLines(filePath);

        TransactionParser parser = new TransactionParser();
        List<Transaction> transactions = parser.parse(rawLines);

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);

        TransactionReportGenerator.printBalanceReport(totalBalance);


        String monthYear = "01-2024";
        int transactionsCount = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear, transactionsCount);

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);
        TransactionReportGenerator.printTopExpensesReport(topExpenses);

        // 1. Перевіряємо найбільші/найменші витрати за грудень 2023
        String targetMonth = "12-2023";
        Transaction largest = TransactionAnalyzer.getLargestExpenseByMonth(transactions, targetMonth);
        Transaction smallest = TransactionAnalyzer.getSmallestExpenseByMonth(transactions, targetMonth);
        TransactionReportGenerator.printMinMaxExpenses(targetMonth, largest, smallest);

        // 2. Будуємо візуальний звіт по категоріях
        Map<String, Double> expensesByCategory = TransactionAnalyzer.getExpensesByCategory(transactions);
        TransactionReportGenerator.printStarReport(expensesByCategory);

    }
}