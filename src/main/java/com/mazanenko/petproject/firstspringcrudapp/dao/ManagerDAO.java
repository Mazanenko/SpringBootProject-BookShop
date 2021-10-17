package com.mazanenko.petproject.firstspringcrudapp.dao;

import com.mazanenko.petproject.firstspringcrudapp.entity.Manager;

import java.util.List;

public interface ManagerDAO {

    void create(Manager manager);

    Manager read(int id);

    Manager readByEmail(String email);

    List<Manager> readAll();

    void update(int id, Manager manager);

    void delete(int id);
}
