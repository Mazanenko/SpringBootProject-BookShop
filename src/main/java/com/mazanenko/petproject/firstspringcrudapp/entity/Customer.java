package com.mazanenko.petproject.firstspringcrudapp.entity;


import javax.validation.constraints.*;

public class Customer extends Person {

    @NotBlank(message = "Must be not empty")
    @Size(min = 4, max = 6, message = "Must be 'male' or 'female'")
    private String gender;

    @NotBlank(message = "Must be not empty")
    private String phone;

    @NotBlank(message = "Must be not empty")
    @Size(min = 6, message = "Must be at least 6 characters")
    private String password;

    private final static String role = "customer";


    private DeliveryAddress deliveryAddress;
    private Cart cart;

    public Customer() {
    }

    public Customer(int id, String name, String surname, String gender, String phone, String email, String password
            , DeliveryAddress deliveryAddress, Cart cart) {
        super(id, name, email, surname);
        this.gender = gender;
        this.phone = phone;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
