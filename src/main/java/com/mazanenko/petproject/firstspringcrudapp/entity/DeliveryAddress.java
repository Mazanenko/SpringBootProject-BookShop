package com.mazanenko.petproject.firstspringcrudapp.entity;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class DeliveryAddress {
    private int customerId;

    @NotBlank(message = "Should be not empty")
    private String country;

    @NotBlank(message = "Should be not empty")
    private String city;

    @NotBlank(message = "Should be not empty")
    private String street;

    @Positive(message = "Should be positive")
    private int houseNumber;

    private String note;

    public DeliveryAddress() {}

    public DeliveryAddress(int customerId, String country, String city, String street, int houseNumber,
                           String note) {
        this.customerId = customerId;
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.note = note;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "DeliveryAddress{" +
                "customerId=" + customerId +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber=" + houseNumber +
                ", note='" + note + '\'' +
                '}';
    }
}
