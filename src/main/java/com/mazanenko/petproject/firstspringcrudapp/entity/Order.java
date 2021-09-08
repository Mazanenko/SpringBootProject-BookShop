package com.mazanenko.petproject.firstspringcrudapp.entity;


public class Order {
    private int cartId;
    private int productId;
    private int quantity;

    private Product product;

    public Order() {
    }

    public Order(int cartId, int productId, int quantity, Product product) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
