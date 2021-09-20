package com.mazanenko.petproject.firstspringcrudapp.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public abstract class Product {

    private int id;

    @NotBlank(message = "Should be not empty")
    private String name;

    @Min(value = 0, message = "Should be greater then zero")
    private int price;

    @NotBlank(message = "Should be not empty")
    private String description;

    @PositiveOrZero(message = "Should be positive or zero")
    private int availableQuantity;


    public Product() {
    }

    public Product(int id, String name, String description, int availableQuantity) {
        this.id = id;
        this.name = name;

        this.description = description;
        this.availableQuantity = availableQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() + '\'' +
                ", availableQuantity='" + getAvailableQuantity() + '\'' +
                '}';
    }
}
