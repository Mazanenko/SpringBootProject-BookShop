package com.mazanenko.petproject.bookshop.utill.modelMapper;

public interface EntityMapper<T, DTO> {
    T toEntity(DTO dto);

    DTO toDto(T entity);
}
