package com.mazanenko.petproject.firstspringcrudapp.entity;

public class Customer extends Person {
    private String surname;
    private int phone;
    private String email;

    private DeliveryAddress deliveryAddress;
    private Cart cart;

    public Customer() {}

    public Customer(int id, String name, String surname, int phone, String email,
                    DeliveryAddress deliveryAddress, Cart cart) {
        super(id, name);
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
        this.cart = cart;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                "surname='" + getSurname() +
                '}';
    }
}
