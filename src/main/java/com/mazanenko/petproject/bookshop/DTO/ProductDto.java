package com.mazanenko.petproject.bookshop.DTO;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mazanenko.petproject.bookshop.entity.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "productType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookDto.class, name = ProductType.Constants.BOOK),
        @JsonSubTypes.Type(value = PosterDto.class, name = ProductType.Constants.POSTER)
})
public abstract class ProductDto {
    @NotNull
    private ProductType productType;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
