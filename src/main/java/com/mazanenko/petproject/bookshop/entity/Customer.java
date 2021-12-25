package com.mazanenko.petproject.bookshop.entity;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

@Entity
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
    private List<Subscription> subscriptions;

    public Customer() {
    }

    public Customer(Long id, String name, String surname, String gender, String phone, String email, String password
            , DeliveryAddress deliveryAddress, Cart cart) {
        super(id, name, email, surname, password, "ROLE_CUSTOMER");
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscribersList) {
        this.subscriptions = subscribersList;
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
