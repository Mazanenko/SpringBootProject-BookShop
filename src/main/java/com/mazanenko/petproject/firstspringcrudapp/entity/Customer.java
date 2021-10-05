package com.mazanenko.petproject.firstspringcrudapp.entity;


import javax.validation.constraints.*;

public class Customer extends Person {

    @NotBlank(message = "Must be not empty")
    @Size(min = 4, max = 6, message = "Must be 'male' or 'female'")
    private String gender;

    @NotBlank(message = "Must be not empty")
    private String phone;

    private DeliveryAddress deliveryAddress;
    private Cart cart;

    public Customer() {
    }

    public Customer(int id, String name, String surname, String gender, String phone, String email, String password
            , DeliveryAddress deliveryAddress, Cart cart, String role) {
        super(id, name, email, surname, password, role);
        this.gender = gender;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
        this.cart = cart;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
                ", surname='" + getSurname() + '\'' +
                "gender='" + getGender() +
                '}';
    }
}
