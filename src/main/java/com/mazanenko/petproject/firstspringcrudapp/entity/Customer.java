package com.mazanenko.petproject.firstspringcrudapp.entity;

public class Customer extends Person {
    private String surname;
    private int phone;
    private String email;

    public Customer() {}

    public Customer(int id, String name, String surname, int phone, String email) {
        super(id, name);
        this.surname = surname;
        this.phone = phone;
        this.email = email;
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
}
