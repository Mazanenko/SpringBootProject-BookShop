package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ManagerService managerService;

    Customer customer = new Customer();
    Manager manager = new Manager();
    String email = "test@mail.ru";

    @BeforeEach
    public void setUp() {
        customer.setEmail(email);
        manager.setEmail(email);
    }

    @Test
    public void findByEmailShouldReturnCustomer() {
        customer.setActivated(true);

        Mockito.doReturn(customer).when(customerService).getCustomerByEmail(email);
        Assertions.assertEquals(userService.findByEmail(email).getEmail(), email);
    }

    @Test
    public void findByEmailShouldReturnManager() {
        Mockito.doReturn(manager).when(managerService).getManagerByEmail(email);
        Assertions.assertEquals(userService.findByEmail(email).getEmail(), email);
    }

    @Test
    public void findByEmailShouldThrowUsernameNotFoundExceptionForCustomer() {
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            Mockito.doReturn(customer).when(customerService).getCustomerByEmail(email);
            userService.findByEmail(email);
        });

        String expectedMessage = String.format("Email %s not activated yet", email);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void findByEmailShouldThrowUsernameNotFoundExceptionForManager() {
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            Mockito.doReturn(null).when(managerService).getManagerByEmail(email);
            userService.findByEmail(email);
        });

        String expectedMessage = String.format("User %s not found", email);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}