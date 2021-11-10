package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.Manager;
import com.mazanenko.petproject.bookshop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Mock
    private CustomerService customerService;

    @Mock
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