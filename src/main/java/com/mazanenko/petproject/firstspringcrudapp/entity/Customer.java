package com.mazanenko.petproject.firstspringcrudapp.entity;


import javax.validation.constraints.*;

public class Customer extends Person {
    @NotBlank(message = "Should be not empty")
    private String surname;

    @NotBlank(message = "Should be not empty")
    @Size(min = 4, max = 6, message = "Should be 'male' or 'female'")
    private String gender;

    @NotBlank(message = "Should be not empty")
    private String phone;

    @NotEmpty(message = "Should be not empty")
    @Email(message = "Not email")
    private String email;

    private DeliveryAddress deliveryAddress;
    private Cart cart;

    public Customer() {}

    public Customer(int id, String name, String surname, String gender, String phone, String email,
                    DeliveryAddress deliveryAddress, Cart cart) {
        super(id, name);
        this.surname = surname;
        this.gender = gender;
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
                ", surname='" + getSurname() + '\'' +
                "gender='" + getGender() +
                '}';
    }
}
