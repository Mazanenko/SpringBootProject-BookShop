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

    @Mock
    private RESTConsumerForManagerEmail restConsumerForManagerEmail;

    private final Manager manager = new Manager();
    private final int id = 1;
    private final String email = "test@mail.ru";
    private final String password = "password";


    @BeforeEach
    void setUp() {
        manager.setId(id);
        manager.setName("Name");
        manager.setSurname("Surname");
        manager.setEmail(email);
        manager.setPassword(password);
    }

    @Test
    void createManagerShouldCreateNewManager() {
        try {
            managerService.createManager(manager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.verify(managerDAO, Mockito.times(1)).create(manager);
    }

    @Test
    void createManagerWhenManagerIsNullShouldThrowException() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            managerService.createManager(null);
        });
        Assertions.assertTrue(exception.getMessage().contains("manager is null"));
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

        try {
            managerService.updateManagerById(id, manager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mockito.verify(managerDAO, Mockito.times(1)).update(id, manager);
    }

    @Test
    void updateManagerByIdWhenPasswordsDontMatch() {
        Manager managerWithNewPassword = new Manager();
        managerWithNewPassword.setEmail(email);
        managerWithNewPassword.setPassword("newPassword");

        Mockito.doReturn(manager).when(managerDAO).read(id);

        try {
            managerService.updateManagerById(id, managerWithNewPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertNotEquals("newPassword", managerWithNewPassword.getPassword());
        try {
            Mockito.verify(restConsumerForManagerEmail, Mockito.times(1))
                    .changePassword(Mockito.anyString(), Mockito.anyString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.verify(managerDAO, Mockito.times(1)).update(id, managerWithNewPassword);
    }

    @Test
    void deleteManagerById() {
        Mockito.doReturn(manager).when(managerDAO).read(id);

        try {
            managerService.deleteManagerById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            Mockito.verify(restConsumerForManagerEmail, Mockito.times(1)).deleteEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.verify(managerDAO, Mockito.times(1)).delete(id);
    }
}