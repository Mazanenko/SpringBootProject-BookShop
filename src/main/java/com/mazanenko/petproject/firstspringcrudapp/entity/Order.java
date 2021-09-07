package com.mazanenko.petproject.firstspringcrudapp.entity;

import java.util.List;

public class Order {
    private int cartId;
    private int productId;
    private int quantity;

    private List<Product> productList;

    public Order() {}

    public Order(int cartId, int productId, int quantity, List<Product> productList) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.productList = productList;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
