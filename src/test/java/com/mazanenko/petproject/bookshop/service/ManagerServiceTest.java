package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.dao.ManagerDAO;
import com.mazanenko.petproject.bookshop.entity.Manager;
import com.mazanenko.petproject.bookshop.service.impl.ManagerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @InjectMocks
    private ManagerService managerService = new ManagerServiceImpl();

    @Mock
    private ManagerDAO managerDAO;

    private final Manager manager = new Manager();
    private final int id = 1;
    private final String email = "test@mail.ru";
    private final String password = "password";


    @BeforeEach
    void setUp() {
        manager.setId(id);
        manager.setEmail(email);
        manager.setPassword(password);
    }

    @Test
    void createManagerShouldCreateNewManager() {
        managerService.createManager(manager);

        Assertions.assertTrue(new BCryptPasswordEncoder().matches(password, manager.getPassword()));
        Mockito.verify(managerDAO, Mockito.times(1)).create(manager);
    }

    @Test
    void createManagerWhenManagerIsNull() {
        managerService.createManager(null);

        Mockito.verifyNoInteractions(managerDAO);
    }

    @Test
    void getManagerByIdShouldReturnCustomer() {
        Mockito.doReturn(manager).when(managerDAO).read(id);

        Assertions.assertEquals(manager, managerService.getManagerById(id));
    }

    @Test
    void getManagerByIdShouldReturnNull() {
        Assertions.assertNull(managerService.getManagerById(0));
        Assertions.assertNull(managerService.getManagerById(-1));
    }

    @Test
    void getManagerByEmail() {
        Mockito.doReturn(manager).when(managerDAO).readByEmail(email);

        Assertions.assertEquals(manager, managerService.getManagerByEmail(email));
    }

    @Test
    void getManagerByEmailShouldReturnNull() {
        Assertions.assertNull(managerService.getManagerByEmail(null));
    }

    @Test
    void getAllManagersShouldReturnSortedListOfManagers() {
        List<Manager> managers = new ArrayList<>();
        Manager manager2 = new Manager();

        manager2.setName("Manager");
        manager2.setEmail("anotherEmail");
        managers.add(manager);
        managers.add(manager2);

        List<Manager> sortedList = managers.stream()
                .sorted(Comparator.comparing(Manager::getId)).collect(Collectors.toList());

        Mockito.doReturn(managers).when(managerDAO).readAll();

        Assertions.assertEquals(sortedList, managerService.getAllManagers());
    }

    @Test
    void getAllManagersShouldNotReturnAdminInListOfManagers() {
        List<Manager> managers = new ArrayList<>();
        Manager manager2 = new Manager();

        manager2.setName("admin");
        manager2.setEmail("admin");
        managers.add(manager);
        managers.add(manager2);

        Mockito.doReturn(managers).when(managerDAO).readAll();

        Assertions.assertFalse(managerService.getAllManagers().contains(manager2));
    }

    @Test
    void updateManagerByIdWhenPasswordsMatch() {
        Mockito.doReturn(manager).when(managerDAO).read(id);

        managerService.updateManagerById(id, manager);

        Mockito.verify(managerDAO, Mockito.times(1)).update(id, manager);
    }

    @Test
    void updateManagerByIdWhenPasswordsDontMatch() {
        Manager managerWithNewPassword = new Manager();
        managerWithNewPassword.setPassword("newPassword");

        Mockito.doReturn(manager).when(managerDAO).read(id);

        managerService.updateManagerById(id, managerWithNewPassword);

        Assertions.assertNotEquals("newPassword", managerWithNewPassword.getPassword());
        Mockito.verify(managerDAO, Mockito.times(1)).update(id, managerWithNewPassword);
    }

    @Test
    void deleteManagerById() {
        managerService.deleteManagerById(id);

        Mockito.verify(managerDAO, Mockito.times(1)).delete(id);
    }
}