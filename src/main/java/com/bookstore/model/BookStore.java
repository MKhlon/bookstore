package com.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book_store")
public class BookStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "available")
    private Integer availableQuantity;
    @Column(name = "booked")
    private Integer bookedQuantity;
    @Column(name = "sold")
    private Integer soldQuantity;
}