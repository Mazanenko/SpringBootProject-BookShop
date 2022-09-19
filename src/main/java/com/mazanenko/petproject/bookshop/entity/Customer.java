package com.mazanenko.petproject.bookshop.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer extends Person {

    @NotBlank(message = "Must be not empty")
    @Size(min = 4, max = 6, message = "Must be 'male' or 'female'")
    @Column(name = "gender")
    private String gender;

    @NotBlank(message = "Must be not empty")
    @Column(name = "phone")
    private String phone;

    @Column(name = "activated")
    private boolean activated;

    @Column(name = "activation_code")
    private String activationCode;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private DeliveryAddress deliveryAddress;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subscription> subscriptions = new HashSet<>();

    public Customer(Long id, String name, String surname, String gender, String phone, String email, String password
            , DeliveryAddress deliveryAddress, Cart cart) {
        super(id, name, surname, email, password, "ROLE_CUSTOMER");
        this.gender = gender;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return activated == customer.activated && gender.equals(customer.gender)
                && Objects.equals(phone, customer.phone) && Objects.equals(activationCode, customer.activationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gender, phone, activated, activationCode);
    }
}
