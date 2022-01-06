package com.mazanenko.petproject.bookshop.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "delivery_address")
public class DeliveryAddress {

    @Id
    @Column(name = "customer_id")
    private Long id;

    @NotBlank(message = "Should be not empty")
    @Column(name = "country")
    private String country;

    @NotBlank(message = "Should be not empty")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "Should be not empty")
    @Column(name = "street")
    private String street;

    @Positive(message = "Should be positive")
    @Column(name = "house_number")
    private int houseNumber;

    @Column(name = "note")
    private String note;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public DeliveryAddress() {}

    public DeliveryAddress(Long id, String country, String city, String street, int houseNumber,
                           String note) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "DeliveryAddress{" +
                "id= " + getId() +
                ", country='" + getCountry() + '\'' +
                ", city='" + getCity() + '\'' +
                ", street='" + getStreet() + '\'' +
                ", houseNumber=" + getHouseNumber() +
                ", note='" + getNote() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryAddress that = (DeliveryAddress) o;
        return houseNumber == that.houseNumber && id.equals(that.id) && Objects.equals(country, that.country)
                && Objects.equals(city, that.city) && Objects.equals(street, that.street)
                && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, city, street, houseNumber, note);
    }
}
