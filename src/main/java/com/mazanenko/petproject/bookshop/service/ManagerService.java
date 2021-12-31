package com.mazanenko.petproject.bookshop.service;


import com.mazanenko.petproject.bookshop.entity.Manager;

import java.util.List;

public interface ManagerService {
    String createManager(Manager manager) throws Exception;

    Manager getManagerById(Long managerId);

    Manager getManagerByEmail(String email);

    List<Manager> getAllManagers();

    void updateManagerById(Long managerId, Manager updatedManager) throws Exception;

    void deleteManagerById(Long managerId) throws Exception;
}
