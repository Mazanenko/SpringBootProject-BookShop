package com.mazanenko.petproject.firstspringcrudapp.dao;


import java.util.List;

public interface DAO<T> {
    void create(T entity);

    T read(int id);

    List<T> readAll();

    void update(int id, T entity);

    void delete(int id);
}
