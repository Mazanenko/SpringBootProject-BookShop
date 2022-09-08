package com.mazanenko.petproject.bookshop.entity;

import com.mazanenko.petproject.bookshop.DTO.PosterDto;
import com.mazanenko.petproject.bookshop.DTO.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(ProductType.Constants.POSTER)
@DynamicUpdate
public class Poster extends Product {
    @Column(name = "theme")
    private String theme;

    @NotNull
    @Min(value = 1, message = "Can't be negative or zero")
    @Column(name = "height")
    private int height;

    @NotNull
    @Min(value = 1, message = "Can't be negative or zero")
    @Column(name = "width")
    private int width;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poster poster)) return false;
        if (!super.equals(o)) return false;

        if (height != poster.height) return false;
        if (width != poster.width) return false;
        return Objects.equals(theme, poster.theme);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (theme != null ? theme.hashCode() : 0);
        result = 31 * result + height;
        result = 31 * result + width;
        return result;
    }

    @Override
    public ProductDto getDto() {
        return new PosterDto();
    }

    @Override
    public Class<PosterDto> getDtoClass() {
        return PosterDto.class;
    }
}
