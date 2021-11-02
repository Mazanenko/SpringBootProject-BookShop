package com.mazanenko.petproject.bookshop.dao.mapper;

import com.mazanenko.petproject.bookshop.entity.Manager;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerMapper implements RowMapper<Manager> {
    @Override
    public Manager mapRow(ResultSet resultSet, int i) throws SQLException {
        Manager manager = new Manager();

        manager.setId(resultSet.getInt("id"));
        manager.setName(resultSet.getString("name"));
        manager.setSurname(resultSet.getString("surname"));
        manager.setEmail(resultSet.getString("email"));
        manager.setPassword(resultSet.getString("password"));

        if (manager.getEmail().equals("admin")) {
            manager.setRole("ROLE_ADMIN");
        } else manager.setRole("ROLE_MANAGER");

        return manager;
    }
}
