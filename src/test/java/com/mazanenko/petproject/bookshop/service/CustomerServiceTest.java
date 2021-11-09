package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.dao.CartDAO;
import com.mazanenko.petproject.bookshop.dao.CustomerDAO;
import com.mazanenko.petproject.bookshop.dao.DeliveryAddressDAO;
import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.Cart;
import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.DeliveryAddress;
import com.mazanenko.petproject.bookshop.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import javax.management.remote.JMXPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService = new CustomerServiceImpl();

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private DeliveryAddressDAO addressDAO;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private final String email = "test@mail.ru";
    private final String password = "password";
    private final int id = 1;
    private final Customer customer = new Customer();
    private final DeliveryAddress address = new DeliveryAddress();
    private final Cart cart = new Cart();
    private final String activationCode = "code";


    @BeforeEach
    void setUp() {
        customer.setName("TestCustomer");
        customer.setId(id);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setDeliveryAddress(address);
        customer.setActivationCode(activationCode);
    }


    @Test
    public void createCustomerShouldCreateNewCustomer() {

        cart.setCustomerId(customer.getId());

        Mockito.doReturn(customer).when(customerDAO).readByEmail(email);

        customerService.createCustomer(customer, address);

        ArgumentCaptor<CartDAO> cartDAOCaptor = ArgumentCaptor.forClass(CartDAO.class);
        ArgumentCaptor<ApplicationEventPublisher> applicationEventPublisherCaptor =
                ArgumentCaptor.forClass(ApplicationEventPublisher.class);

        Mockito.verify(customerDAO, Mockito.times(1)).create(customer);
        Mockito.verify(addressDAO, Mockito.times(1)).create(address);
        Mockito.verify(cartDAO, Mockito.times(1)).create((Cart) cartDAOCaptor.capture());
        Mockito.verify(applicationEventPublisher, Mockito.times(1)).publishEvent(applicationEventPublisherCaptor.capture());
    }

    @Test
    void getCustomerByIdShouldReturnCustomer() {
        Mockito.doReturn(customer).when(customerDAO).read(id);

        Assertions.assertEquals(customer, customerService.getCustomerById(customer.getId()));

        Mockito.verify(addressDAO, Mockito.times(1)).read(id);
        Mockito.verify(cartDAO, Mockito.times(1)).readByCustomerId(id);
    }

    @Test
    void getCustomerByIdShouldReturnNull() {
        Mockito.doReturn(null).when(customerDAO).read(id);

        Assertions.assertNull(customerService.getCustomerById(customer.getId()));

        Mockito.verifyNoInteractions(addressDAO);
        Mockito.verifyNoInteractions(cartDAO);
    }

    @Test
    void getCustomerByEmailShouldReturnCustomer() {
        Mockito.doReturn(customer).when(customerDAO).readByEmail(email);

        Assertions.assertEquals(customer, customerService.getCustomerByEmail(customer.getEmail()));

        Mockito.verify(addressDAO, Mockito.times(1)).read(id);
        Mockito.verify(cartDAO, Mockito.times(1)).readByCustomerId(id);
    }

    @Test
    void getCustomerByEmailShouldReturnNull() {
        Mockito.doReturn(null).when(customerDAO).readByEmail(email);

        Assertions.assertNull(customerService.getCustomerByEmail(customer.getEmail()));

        Mockito.verifyNoInteractions(addressDAO);
        Mockito.verifyNoInteractions(cartDAO);
    }

    @Test
    void getAllCustomersShouldReturnSortedListOfCustomers() {
        List<Customer> customers = new ArrayList<>();
        Customer customer2 = new Customer();

        customer2.setName("Customer");
        customers.add(customer);
        customers.add(customer2);

        List<Customer> sortedList = customers.stream()
                .sorted(Comparator.comparing(Customer::getId)).collect(Collectors.toList());

        Mockito.doReturn(customers).when(customerDAO).readAll();

        Assertions.assertEquals(sortedList, customerService.getAllCustomers());
    }

    @Test
    void updateCustomerByIdWhenPasswordsMatch() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setPassword(password);
        updatedCustomer.setActivated(false);

        Mockito.doReturn(customer).when(customerDAO).read(id);

        customerService.updateCustomerById(id, updatedCustomer, address);

        Assertions.assertTrue(updatedCustomer.isActivated());
        Mockito.verify(customerDAO, Mockito.times(1)).update(id, updatedCustomer);
        Mockito.verify(addressDAO, Mockito.times(1)).update(id, address);
    }

    @Test
    void updateCustomerByIdWhenPasswordsDontMatch() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setPassword("anotherPassword");
        updatedCustomer.setActivated(false);

        Mockito.doReturn(customer).when(customerDAO).read(id);

        customerService.updateCustomerById(id, updatedCustomer, address);

        Assertions.assertTrue(updatedCustomer.isActivated());
        Assertions.assertNotEquals("anotherPassword", updatedCustomer.getPassword());
        Mockito.verify(customerDAO, Mockito.times(1)).update(id, updatedCustomer);
        Mockito.verify(addressDAO, Mockito.times(1)).update(id, address);
    }

    @Test
    void deleteCustomerById() {
        customerService.deleteCustomerById(id);
        Mockito.verify(customerDAO, Mockito.times(1)).delete(id);
    }

    @Test
    void deleteCustomerByIdWhenIdLessThenOne() {
        customerService.deleteCustomerById(0);
        Mockito.verifyNoInteractions(cartDAO);
    }

    @Test
    void deleteCustomerByEmail() {
        customerService.deleteCustomerByEmail(email);
        Mockito.verify(customerDAO, Mockito.times(1)).deleteByEmail(email);
    }

    @Test
    void deleteCustomerByEmailWhenEmailIsNull() {
        customerService.deleteCustomerByEmail(null);
        Mockito.verifyNoInteractions(cartDAO);
    }

    @Test
    void activateUserShouldReturnTrue() {
        Mockito.doReturn(customer).when(customerDAO).readByActivationCode(activationCode);

        Assertions.assertTrue(customerService.activateUser(activationCode));
        Assertions.assertNull(customer.getActivationCode());
        Assertions.assertTrue(customer.isActivated());

        Mockito.verify(customerDAO, Mockito.times(1)).update(id, customer);
    }

    @Test
    void activateUserShouldReturnFalse() {
        Mockito.doReturn(null).when(customerDAO).readByActivationCode(activationCode);

        Assertions.assertFalse(customerService.activateUser(activationCode));
        Assertions.assertEquals(activationCode, customer.getActivationCode());
        Assertions.assertFalse(customer.isActivated());

        Mockito.verifyNoMoreInteractions(customerDAO);
    }

    @Test
    void isSubscribedToArrivalReturnTrue() {
        Principal principal = new JMXPrincipal(email);
        Book book = new Book();
        book.setSubscribersList(new ArrayList<>(List.of(id)));

        Mockito.doReturn(customer).when(customerDAO).readByEmail(email);

        Assertions.assertTrue(customerService.isSubscribedToArrival(principal, book));
    }

    @Test
    void isSubscribedToArrivalReturnFalseWhenSubscribersListIsEmpty() {
        Principal principal = new JMXPrincipal(email);
        Book book = new Book();
        book.setSubscribersList(new ArrayList<>());

        Mockito.doReturn(customer).when(customerDAO).readByEmail(email);

        Assertions.assertFalse(customerService.isSubscribedToArrival(principal, book));
    }

    @Test
    void isSubscribedToArrivalReturnFalseWhenSubscribersListDoesNotContainsCustomerId() {
        Principal principal = new JMXPrincipal(email);
        Book book = new Book();
        book.setSubscribersList(new ArrayList<>(List.of(id+1)));

        Mockito.doReturn(customer).when(customerDAO).readByEmail(email);

        Assertions.assertFalse(customerService.isSubscribedToArrival(principal, book));
    }
}