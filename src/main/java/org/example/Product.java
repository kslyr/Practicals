package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Product {
    private int id; // Унікальний ідентифікатор товару
    private String name; // Назва товару
    private BigDecimal price; // Ціна товару
    private String description; // Опис товару
    private Category category;
}
