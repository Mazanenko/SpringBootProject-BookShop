package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.ManagerDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Manager;
import com.mazanenko.petproject.firstspringcrudapp.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerDAO managerDAO;

    @Autowired
    public ManagerServiceImpl(ManagerDAO managerDAO) {
        this.managerDAO = managerDAO;
    }

    @Override
    public void createManager(Manager manager) {
        String cryptedPassword = BCrypt.hashpw(manager.getPassword(), BCrypt.gensalt());
        manager.setPassword(cryptedPassword);
        managerDAO.create(manager);
    }

    @Override
    public Manager getManagerById(int id) {
        return managerDAO.read(id);
    }

    @Override
    public Manager getManagerByEmail(String email) {
        return managerDAO.readByEmail(email);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerDAO.readAll();
    }

    @Override
    public void updateManagerById(int id, Manager manager) {
        if (!(manager.getPassword().equals(managerDAO.read(id).getPassword()))) {
            String cryptedPassword = BCrypt.hashpw(manager.getPassword(), BCrypt.gensalt());
            manager.setPassword(cryptedPassword);
        }
        managerDAO.update(id, manager);
    }

    @Override
    public void deleteManagerById(int id) {
        managerDAO.delete(id);
    }
}
