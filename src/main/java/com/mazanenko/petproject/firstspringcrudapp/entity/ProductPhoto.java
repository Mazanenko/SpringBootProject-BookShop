package com.mazanenko.petproject.firstspringcrudapp.entity;

public class ProductPhoto {
    private int productId;
    private String URL;

    public ProductPhoto() {}

    public ProductPhoto(int productId, String URL) {
        this.URL = URL;
        this.productId = productId;
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
                ", productId=" + getProductId() +
                '}';
    }
}
