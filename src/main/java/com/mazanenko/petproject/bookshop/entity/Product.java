package com.mazanenko.petproject.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", length = 150)
@Entity
@Table(name = "products")
public abstract class Product extends BaseEntity {

    @NotBlank(message = "Should be not empty")
    @Column(name = "name")
    private String name;

    @Min(value = 0, message = "Should be greater then zero")
    @Column(name = "price")
    private int price;

    @NotBlank(message = "Should be not empty")
    @Column(name = "description")
    private String description;

    @PositiveOrZero(message = "Should be positive or zero")
    @Column(name = "available_quantity")
    private int availableQuantity;

    public Product(Long id, String name, String description, int availableQuantity, int price) {
        super(id);
        this.name = name;
        this.description = description;
        this.availableQuantity = availableQuantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() + '\'' +
                ", availableQuantity='" + getAvailableQuantity() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        if (!super.equals(o)) return false;

        if (price != product.price) return false;
        if (availableQuantity != product.availableQuantity) return false;
        if (!Objects.equals(name, product.name)) return false;
        return Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + availableQuantity;
        return result;
    }
}
