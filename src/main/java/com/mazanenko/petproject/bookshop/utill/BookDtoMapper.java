package com.mazanenko.petproject.bookshop.utill;

import com.mazanenko.petproject.bookshop.DTO.BookDto;
import com.mazanenko.petproject.bookshop.entity.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BookDtoMapper {
    private final ModelMapper modelMapper;

    public Book toEntity(BookDto bookDto) {
        return Objects.isNull(bookDto) ? null : modelMapper.map(bookDto, Book.class);
    }

    public BookDto toDto(Book book) {
        return Objects.isNull(book) ? null : modelMapper.map(book, BookDto.class);
    }

    public Book updateFromDto(BookDto bookDto, Book book) {
        if (bookDto == null) throw new IllegalArgumentException("bookDto can't bee null.");
        modelMapper.map(bookDto, book);
        return book;
    }

     /*
    default T toEntity(DTO dto, Class<T> entityClass, ModelMapper modelMapper) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, entityClass);
    }

    default DTO toDto(T entity, Class<DTO> dtoClass, ModelMapper modelMapper) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, dtoClass);
    }

    default T updateFromDto(DTO sourceDto, T targetEntity, ModelMapper modelMapper) {
        if (sourceDto == null) throw new IllegalArgumentException("bookDto can't bee null.");
        modelMapper.map(sourceDto, targetEntity);
        return targetEntity;
    }
     */
}
