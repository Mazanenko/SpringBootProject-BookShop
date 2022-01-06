package com.mazanenko.petproject.bookshop.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@MappedSuperclass
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Should be not empty")
    private String name;

    @Column(name = "surname")
    @NotBlank(message = "Should be not empty")
    private String surname;

    @Column(name = "email")
    @NotEmpty(message = "Should be not empty")
    @Email(message = "Not email")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Must be not empty")
    @Size(min = 6, message = "Must be at least 6 characters")
    private String password;

    @Transient
    private String role;

    public Person() {
    }

    public Person(Long id, String name, String surname, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", name='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id) && name.equals(person.name) && Objects.equals(surname, person.surname)
                && email.equals(person.email) && password.equals(person.password)
                && Objects.equals(role, person.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, password, role);
    }
}
