package com.mj.payroll.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class Product {
    private @Id
    @GeneratedValue Long id;

    private String category;
    private Double price;
    private String name;

    public Product(String category, double price, String name) {
        this.category = category;
        this.price = price;
        this.name = name;
    }
}
