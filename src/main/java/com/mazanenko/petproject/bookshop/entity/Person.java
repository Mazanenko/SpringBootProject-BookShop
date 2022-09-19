package com.mazanenko.petproject.bookshop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
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

    public Person(Long id, String name, String surname, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
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
