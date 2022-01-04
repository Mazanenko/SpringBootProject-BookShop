package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Manager;
import com.mazanenko.petproject.bookshop.repository.ManagerRepository;
import com.mazanenko.petproject.bookshop.service.impl.ManagerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @InjectMocks
    private ManagerService managerService = Mockito.spy(new ManagerServiceImpl());

    @Mock
    private ManagerRepository managerRepo;

    @Mock
    private RESTConsumerForManagerEmail restConsumerForManagerEmail;

    private final Manager manager = new Manager();
    private final Long id = 1L;
    private final String email = "test@mail.ru";
    private final String password = "password";


    @BeforeEach
    void setUp() {
        manager.setId(id);
        manager.setName("Name");
        manager.setSurname("Surname");
    }

    @Test
    void createManagerShouldCreateNewManager() throws Exception {
        //When
        Mockito.when(restConsumerForManagerEmail.createEmail(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        try {
            managerService.createManager(manager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Then
        Mockito.verify(managerRepo, Mockito.times(1)).saveAndFlush(manager);
    }

    @Test
    void createManagerShouldThrowExceptionWhenManagerIsNull() {
        //When
        Exception exception = Assertions.assertThrows(Exception.class, () -> managerService.createManager(null));

        //Then
        Assertions.assertEquals("manager is null", exception.getMessage());
        Mockito.verifyNoInteractions(managerRepo);
        Mockito.verifyNoInteractions(restConsumerForManagerEmail);
    }

    @Test
    void createManagerShouldGenerateAnotherEmailWhenCurrentIsOccupiedInEmailService() throws Exception {
        //When
        Mockito.when(restConsumerForManagerEmail
                .createEmail(Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        try {
            managerService.createManager(manager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Then
        Mockito.verify(managerService, Mockito.times(2)).createManager(manager);
        Mockito.verify(managerRepo, Mockito.times(1)).saveAndFlush(manager);
    }

    @Test
    void createManagerShouldGenerateAnotherEmailWhenCurrentIsOccupiedInDatabase() throws Exception {
        //When
        Mockito.when(restConsumerForManagerEmail
                .createEmail(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        Mockito.when(managerRepo.saveAndFlush(manager))
                .thenThrow(new DataIntegrityViolationException("email_unique", new Exception("email_unique")))
                .thenReturn(manager);

        try {
            managerService.createManager(manager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Then
        Mockito.verify(managerService, Mockito.times(2)).createManager(manager);
        Mockito.verify(managerRepo, Mockito.times(2)).saveAndFlush(manager);
    }

    @Test
    void getManagerByIdShouldReturnManager() {
        //Given an id > 0
        String role = "ROLE_MANAGER";
        manager.setEmail("manager@mail.com");

        //When
        Mockito.when(managerRepo.findById(id)).thenReturn(Optional.of(manager));

        //Then
        Assertions.assertEquals(manager, managerService.getManagerById(id));
        Assertions.assertEquals(manager.getRole(), role);
    }

    @Test
    void getManagerByIdShouldReturnAdmin() {
        //Given an id > 0
        String role = "ROLE_ADMIN";
        manager.setEmail("admin");

        //When
        Mockito.when(managerRepo.findById(id)).thenReturn(Optional.of(manager));

        //Then
        Assertions.assertEquals(manager, managerService.getManagerById(id));
        Assertions.assertEquals(manager.getRole(), role);
    }

    @Test
    void getManagerByIdShouldReturnNull() {
        // When id is zero or less than zero
        //Than
        Assertions.assertNull(managerService.getManagerById(0L));
        Assertions.assertNull(managerService.getManagerById(-1L));
    }

    @Test
    void getManagerByIdShouldReturnNullWhenEmailIsNull() {
        //Given
        String email = null;
        // When
        manager.setEmail(email);
        //Than
        Assertions.assertNull(managerService.getManagerById(id));
    }

    @Test
    void getManagerByEmailShouldReturnManager() {
        //Given an id > 0
        String role = "ROLE_MANAGER";
        String email = "manager@mail.com";
        manager.setEmail(email);

        //When
        Mockito.when(managerRepo.findByEmail(email)).thenReturn(Optional.of(manager));

        //Then
        Assertions.assertEquals(manager, managerService.getManagerByEmail(email));
        Assertions.assertEquals(manager.getRole(), role);
    }

    @Test
    void getManagerByEmailShouldReturnAdmin() {
        //Given an id > 0
        String role = "ROLE_ADMIN";
        String email = "admin";
        manager.setEmail(email);

        //When
        Mockito.when(managerRepo.findByEmail(email)).thenReturn(Optional.of(manager));

        //Then
        Assertions.assertEquals(manager, managerService.getManagerByEmail(email));
        Assertions.assertEquals(manager.getRole(), role);
    }

    @Test
    void getManagerByEmailShouldReturnNull() {
        //Given
        String email = null;

        //When
        manager.setEmail(email);

        //Then
        Assertions.assertNull(managerService.getManagerByEmail(null));
    }

    @Test
    void getAllManagersShouldReturnSortedListOfManagersAcceptAdmin() {
        //Given
        List<Manager> managers = new ArrayList<>();
        Manager manager2 = new Manager();
        Manager admin = new Manager();

        manager.setEmail("manager@mail.com");

        manager2.setId(2L);
        manager2.setName("Manager");
        manager2.setEmail("another_manager@mail.com");

        admin.setId(3L);
        admin.setName("admin");
        admin.setEmail("admin");

        managers.add(manager);
        managers.add(manager2);
        managers.add(admin);

        List<Manager> sortedList = managers.stream()
                .sorted(Comparator.comparing(Manager::getId)).collect(Collectors.toList());

        //When
        Mockito.when(managerRepo.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(sortedList);

        //Then
        Assertions.assertEquals(sortedList, managerService.getAllManagers());
        Assertions.assertFalse(managerService.getAllManagers().contains(admin));
    }

    @Test
    void updateManagerByIdWhenPasswordsMatch() {
        //Given manager as managerFromDatabase
        manager.setEmail(email);
        manager.setPassword(password);

        Manager updatedManager = new Manager(2L, "updatedManager", "updatedManager",
                "updated_manager", password);

        //When
        Mockito.when(managerRepo.findById(id)).thenReturn(Optional.of(manager));
        try {
            managerService.updateManagerById(id, updatedManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Then
        Mockito.verifyNoInteractions(restConsumerForManagerEmail);
        Mockito.verify(managerRepo, Mockito.times(1)).save(updatedManager);
    }

    @Test
    void updateManagerByIdWhenPasswordsDontMatch() throws Exception {
        //Given manager as managerFromDatabase
        manager.setEmail(email);
        manager.setPassword(password);

        String newPass = "new_pass";
        Manager updatedManager = new Manager(2L, "updatedManager", "updatedManager",
                "updated_manager", newPass);

        //When
        Mockito.when(managerRepo.findById(id)).thenReturn(Optional.of(manager));
        Mockito.when(restConsumerForManagerEmail.changePassword(updatedManager.getEmail(),
                updatedManager.getPassword())).thenReturn(true);
        try {
            managerService.updateManagerById(id, updatedManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Then
        Mockito.verify(restConsumerForManagerEmail, Mockito.times(1))
                .changePassword(updatedManager.getEmail(), newPass);
        Mockito.verify(managerRepo, Mockito.times(1)).save(updatedManager);
        Assertions.assertTrue(BCrypt.checkpw(newPass, updatedManager.getPassword()));
    }

    @Test
    void deleteManagerById() {
        //Given id > 0
        manager.setEmail(email);

        //When
        Mockito.when(managerRepo.findById(id)).thenReturn(Optional.of(manager));

        try {
            managerService.deleteManagerById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Then
        try {
            Mockito.verify(restConsumerForManagerEmail, Mockito.times(1)).deleteEmail(email);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Mockito.verify(managerRepo, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteManagerByIdWhenIdIsZeroOrLess() {
        //Given
        Long invalidId = 0L;

        //When
        try {
            managerService.deleteManagerById(invalidId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Then
        Mockito.verifyNoInteractions(managerRepo);
        Mockito.verifyNoInteractions(restConsumerForManagerEmail);
    }

    @Test
    void deleteManagerByIdWhenReturnedManagerIsNull() {
        //Given id > 0

        //When
        Mockito.when(managerRepo.findById(id)).thenReturn(null);
        try {
            managerService.deleteManagerById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Then
        Mockito.verify(managerRepo, Mockito.times(1)).findById(id);
        Mockito.verifyNoMoreInteractions(managerRepo);
        Mockito.verifyNoInteractions(restConsumerForManagerEmail);
    }
}