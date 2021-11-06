package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.Manager;
import org.junit.jupiter.api.Assertions;
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


    @Test
    public void findByEmailShouldReturnCustomer() {
        Customer customer = new Customer();
        String email = "test@mail.ru";

        customer.setEmail(email);
        customer.setActivated(true);

        Mockito.doReturn(customer).when(customerService).getCustomerByEmail(email);
        Assertions.assertEquals(userService.findByEmail(email).getEmail(), email);
    }

    @Test
    public void findByEmailShouldReturnManager() {
        Manager manager = new Manager();
        String email = "test@mail.com";

        manager.setEmail(email);

        Mockito.doReturn(manager).when(managerService).getManagerByEmail(email);
        Assertions.assertEquals(userService.findByEmail(email).getEmail(), email);
    }

    @Test
    public void findByEmailShouldThrowUsernameNotFoundExceptionForCustomer() {
        String email = "test@mail.com";
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            Mockito.doReturn(new Customer()).when(customerService).getCustomerByEmail(email);
            userService.findByEmail(email);
        });

        String expectedMessage = String.format("Email %s not activated yet", email);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void findByEmailShouldThrowUsernameNotFoundExceptionForManager() {
        String email = "test@mail.com";
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            Mockito.doReturn(null).when(managerService).getManagerByEmail(email);
            userService.findByEmail(email);
        });

        String expectedMessage = String.format("User %s not found", email);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}