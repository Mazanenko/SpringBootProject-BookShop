package com.mazanenko.petproject.firstspringcrudapp.entity;

import javax.validation.constraints.NotBlank;

public abstract class Person {
    private int id;

    @NotBlank(message = "Should be not empty")
    private String name;

    public Person() {}

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
