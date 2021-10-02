package com.mazanenko.petproject.firstspringcrudapp.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public abstract class Person {

    private int id;

    @NotBlank(message = "Should be not empty")
    private String name;

    @NotBlank(message = "Should be not empty")
    protected String surname;

    @NotEmpty(message = "Should be not empty")
    @Email(message = "Not email")
    protected String email;

    public Person() {}

    public Person(int id, String name, String email, String surname) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
