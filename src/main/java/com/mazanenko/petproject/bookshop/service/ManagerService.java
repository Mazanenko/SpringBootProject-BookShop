package com.mazanenko.petproject.bookshop.service;


import com.mazanenko.petproject.bookshop.entity.Manager;

import java.util.List;

public interface ManagerService {
    String createManager(Manager manager) throws Exception;

    Manager getManagerById(int id);

    Manager getManagerByEmail(String email);

    List<Manager> getAllManagers();

    void updateManagerById(int id, Manager manager) throws Exception;

    void deleteManagerById(int id) throws Exception;
}
