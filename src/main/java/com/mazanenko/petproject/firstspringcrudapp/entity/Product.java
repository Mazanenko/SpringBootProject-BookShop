package com.mazanenko.petproject.firstspringcrudapp.entity;

public abstract class Product {
    private int id;
    private String name;
    private int price;
    private String description;
    private int availableQuantity;

    private ProductPhoto productPhoto;

    public Product() {}

    public Product(int id, String name, String description, int availableQuantity,
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ProductPhoto getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(ProductPhoto productPhoto) {
        this.productPhoto = productPhoto;
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
