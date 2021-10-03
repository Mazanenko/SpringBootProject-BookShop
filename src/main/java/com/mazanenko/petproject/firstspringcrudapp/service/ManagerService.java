package com.mazanenko.petproject.firstspringcrudapp.service;


import com.mazanenko.petproject.firstspringcrudapp.entity.Manager;

import java.util.List;

public interface ManagerService {
    void createManager(Manager manager);

    Manager getManagerById(int id);

    Manager getManagerByEmail(String email);

    List<Manager> getAllManagers();

    void updateManagerById(int id, Manager manager);

    void deleteManagerById(int id);
}
