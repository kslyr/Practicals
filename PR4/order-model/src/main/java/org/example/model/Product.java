package org.example.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Product {
    private final String name;
    private final double price;

    protected Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
