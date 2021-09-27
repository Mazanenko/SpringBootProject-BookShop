package com.mazanenko.petproject.firstspringcrudapp.entity;


public class Order {
    private int id;
    private int cartId;
    private int productId;
    private int quantity;

    // no need?
//    private Product product;

    public Order() {
    }

    public Order(int cartId, int productId, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Order{" +
                "cartId=" + getCartId() +
                ", productId=" + getProductId() +
                ", product quantity=" + getQuantity() +
                '}';
    }
}
