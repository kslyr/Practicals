package org.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class Order<T extends Product> {
    private final long id;
    private final T product;
    private final int quantity;
}
