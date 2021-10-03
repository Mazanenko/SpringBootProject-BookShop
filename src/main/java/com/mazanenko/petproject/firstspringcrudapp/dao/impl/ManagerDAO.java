package com.mazanenko.petproject.firstspringcrudapp.dao.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.DAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.mapper.ManagerMapper;
import com.mazanenko.petproject.firstspringcrudapp.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManagerDAO implements DAO<Manager> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ManagerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Manager manager) {
        jdbcTemplate.update("INSERT INTO manager (name, surname, email, password) VALUES (?, ?, ?, ?)"
                , manager.getName(), manager.getSurname(), manager.getEmail(), manager.getPassword());
    }

    @Override
    public Manager read(int id) {
        return jdbcTemplate.query("SELECT * FROM manager WHERE id=?", new ManagerMapper(), id)
                .stream().findAny().orElse(null);
    }

    public Manager readByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM manager WHERE email=?", new ManagerMapper(), email)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Manager> readAll() {
        return jdbcTemplate.query("SELECT * FROM manager", new ManagerMapper());
    }

    @Override
    public void update(int id, Manager manager) {
        jdbcTemplate.update("UPDATE manager SET name = ?, surname = ?, email = ?, password = ? WHERE id = ?"
                , manager.getName(), manager.getSurname(), manager.getEmail(), manager.getPassword(), id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM manager WHERE id = ?", id);
    }
}
