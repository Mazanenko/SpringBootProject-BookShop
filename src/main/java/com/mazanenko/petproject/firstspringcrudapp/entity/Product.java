package com.mazanenko.petproject.firstspringcrudapp.entity;

public abstract class Product {
    private int id;
    private String name;
    private String description;
    private String availableQuantity;

    private ProductPhoto productPhoto;

    public Product() {}

    public Product(int id, String name, String description, String availableQuantity,
                   ProductPhoto productPhoto) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.availableQuantity = availableQuantity;
        this.productPhoto = productPhoto;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
