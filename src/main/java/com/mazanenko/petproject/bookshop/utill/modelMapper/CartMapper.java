package com.mazanenko.petproject.bookshop.utill.modelMapper;

import com.mazanenko.petproject.bookshop.DTO.CartDto;
import com.mazanenko.petproject.bookshop.entity.Cart;

public interface CartMapper extends EntityMapper<Cart, CartDto> {
    Cart toEntity(CartDto cartDto);

    CartDto toDto(Cart cart);

}
