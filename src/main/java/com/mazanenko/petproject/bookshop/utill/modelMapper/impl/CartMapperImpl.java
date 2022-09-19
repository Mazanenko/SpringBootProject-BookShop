package com.mazanenko.petproject.bookshop.utill.modelMapper.impl;

import com.mazanenko.petproject.bookshop.DTO.CartDto;
import com.mazanenko.petproject.bookshop.entity.Cart;
import com.mazanenko.petproject.bookshop.utill.modelMapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CartMapperImpl implements CartMapper {
    private final ModelMapper modelMapper;
    @Override
    public Cart toEntity(CartDto cartDto) {
        return Objects.isNull(cartDto) ? null : modelMapper.map(cartDto, Cart.class);
    }

    @Override
    public CartDto toDto(Cart cart) {
        return Objects.isNull(cart) ? null : modelMapper.map(cart, CartDto.class);
    }
}
