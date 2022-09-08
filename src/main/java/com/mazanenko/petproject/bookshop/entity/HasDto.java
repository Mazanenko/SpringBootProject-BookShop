package com.mazanenko.petproject.bookshop.entity;

public interface HasDto<T> {
    T getDto();
    Class<? extends T> getDtoClass();
}
