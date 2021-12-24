package com.mazanenko.petproject.bookshop.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "manager")
public final class Manager extends Person {

    public Manager() {
    }

    public Manager(Long id, String name, String surname, String email, String password) {
        super(id, name, surname, email, password, "ROLE_MANAGER");

        if (email.equals("admin")) {
            this.setRole("ROLE_ADMIN");
        }
    }
}
