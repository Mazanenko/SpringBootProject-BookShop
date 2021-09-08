package com.mazanenko.petproject.firstspringcrudapp.entity;

public class ProductPhoto {
    private int id;
    private String URL;
    private int productId;

    public ProductPhoto() {}

    public ProductPhoto(int id, String URL, int productId) {
        this.id = id;
        this.URL = URL;
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ProductPhoto{" +
                "id=" + getId() +
                ", productId=" + getProductId() +
                '}';
    }
}
