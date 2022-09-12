package com.mazanenko.petproject.bookshop.entity.event;

import com.mazanenko.petproject.bookshop.entity.Product;

public class ProductArrivalEvent {
    private final Product product;


    public ProductArrivalEvent(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
