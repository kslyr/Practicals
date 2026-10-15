package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Transaction {
        private String date;
        private double amount;
        private String description;

        @Override
        public String toString() {
            return "Transaction{" +
                    "date='" + date + '\'' +
                    ", amount=" + amount +
                    ", description='" + description + '\'' +
                    '}';
        }
}
