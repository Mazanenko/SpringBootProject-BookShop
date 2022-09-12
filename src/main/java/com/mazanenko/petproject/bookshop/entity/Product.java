package com.mazanenko.petproject.bookshop.entity;

import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", length = 150)
@Entity
@Table(name = "products")
public abstract class Product extends BaseEntity implements HasDto<ProductDto> {

    @Column(name = "product_type", insertable = false, updatable = false)
    private String productType;

    @NotBlank(message = "Should be not empty")
    @Column(name = "name")
    private String name;

    @Min(value = 0, message = "Should be greater then zero")
    @Column(name = "price")
    private BigDecimal price;

    @NotBlank(message = "Should be not empty")
    @Column(name = "description")
    private String description;

    @PositiveOrZero(message = "Should be positive or zero")
    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @OneToMany(mappedBy = "product")
    private List<Subscription> subscribersList = new ArrayList<>();

    public Product(String name, String description, Integer availableQuantity, BigDecimal price) {
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

        if (!productType.equals(product.productType)) return false;
        if (!name.equals(product.name)) return false;
        if (!Objects.equals(price, product.price)) return false;
        if (!Objects.equals(description, product.description)) return false;
        return Objects.equals(availableQuantity, product.availableQuantity);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + productType.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (availableQuantity != null ? availableQuantity.hashCode() : 0);
        return result;
    }
}
