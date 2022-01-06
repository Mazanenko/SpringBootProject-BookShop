package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.*;
import com.mazanenko.petproject.bookshop.repository.CustomerRepository;
import com.mazanenko.petproject.bookshop.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService = new CustomerServiceImpl();

    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private CartService cartService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private final String EMAIL = "test_customer@mail.ru";
    private final String PASSWORD = "password";
    private final Long ID = 1L;
    private final Customer CUSTOMER = new Customer();
    private final DeliveryAddress ADDRESS = new DeliveryAddress();
    private final Cart CART = new Cart();
    private final String ACTIVATION_CODE = "code";
    private final Book BOOK = new Book();
    private final Subscription SUBSCRIPTION = new Subscription(BOOK, CUSTOMER);


    @BeforeEach
    void setUp() {
        ADDRESS.setId(ID);

        CUSTOMER.setName("TestCustomer");
        CUSTOMER.setId(ID);
        CUSTOMER.setEmail(EMAIL);
        CUSTOMER.setPassword(PASSWORD);
        CUSTOMER.setDeliveryAddress(ADDRESS);
        CUSTOMER.setActivationCode(ACTIVATION_CODE);
    }


    @Test
    public void createCustomerShouldCreateNewCustomer() {
        //Given
        ADDRESS.setId(CUSTOMER.getId());

        //When
        customerService.createCustomer(CUSTOMER, ADDRESS);

        ArgumentCaptor<ApplicationEventPublisher> applicationEventPublisherCaptor = ArgumentCaptor
                .forClass(ApplicationEventPublisher.class);

        //Then
        Mockito.verify(customerRepo, Mockito.times(1)).save(CUSTOMER);
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(applicationEventPublisherCaptor.capture());
    }

    @Test
    public void createCustomerWhenCustomerOrAddressIsNull() {
        //Given customer and address are null

        //When
        customerService.createCustomer(null, null);

        //Then
        Mockito.verifyNoInteractions(customerRepo);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void getCustomerByIdShouldReturnCustomer() {
        //Given
        String role = "ROLE_CUSTOMER";

        //When
        Mockito.when(customerRepo.findById(ID)).thenReturn(Optional.of(CUSTOMER));

        //Then
        Assertions.assertEquals(CUSTOMER, customerService.getCustomerById(ID));
        Assertions.assertEquals(role, CUSTOMER.getRole());

    }

    @Test
    void getCustomerByIdShouldReturnNull() {
        //When customerId is zero or less

        //Then
        Assertions.assertNull(customerService.getCustomerById(0L));
        Mockito.verifyNoInteractions(customerRepo);
        Assertions.assertNull(CUSTOMER.getRole());
    }

    @Test
    void getCustomerByIdShouldReturnNullWhenReturnedCustomerIsNotExist() {
        //When
        Mockito.when(customerRepo.findById(ID)).thenReturn(Optional.empty());

        //Then
        Assertions.assertNull(customerService.getCustomerById(ID));
        Assertions.assertNull(CUSTOMER.getRole());
    }

    @Test
    void getCustomerByEmailShouldReturnCustomer() {
        //Given
        String role = "ROLE_CUSTOMER";

        //When
        Mockito.when(customerRepo.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER));

        //Then
        Assertions.assertEquals(CUSTOMER, customerService.getCustomerByEmail(EMAIL));
        Assertions.assertEquals(role, CUSTOMER.getRole());
    }

    @Test
    void getCustomerByEmailShouldReturnNull() {
        //When email is null

        //Then
        Assertions.assertNull(customerService.getCustomerByEmail(null));
        Mockito.verifyNoInteractions(customerRepo);
        Assertions.assertNull(CUSTOMER.getRole());
    }

    @Test
    void getCustomerByEmailShouldReturnNullWhenReturnedCustomerIsNotExist() {
        //When
        Mockito.when(customerRepo.findByEmail(EMAIL)).thenReturn(Optional.empty());

        //Then
        Assertions.assertNull(customerService.getCustomerByEmail(EMAIL));
        Assertions.assertNull(CUSTOMER.getRole());
    }

    @Test
    void getAllCustomersShouldReturnSortedListOfCustomers() {
        //Given
        List<Customer> customers = new ArrayList<>();
        Customer customer2 = new Customer();

        customer2.setId(2L);
        customer2.setName("NewCustomer");
        customers.add(CUSTOMER);
        customers.add(customer2);

        List<Customer> sortedList = customers.stream()
                .sorted(Comparator.comparing(Customer::getId)).collect(Collectors.toList());

        //When
        Mockito.when(customerRepo.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(sortedList);

        //Then
        Assertions.assertEquals(sortedList, customerService.getAllCustomers());
    }

    @Test
    void updateCustomerByIdWhenPasswordsMatch() {
        //Given customer as originalCustomer
        List<Subscription> list = new ArrayList<>();
        list.add(SUBSCRIPTION);
        CUSTOMER.setSubscriptions(list);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(2L);
        updatedCustomer.setPassword(PASSWORD);
        updatedCustomer.setActivated(false);

        //When
        Mockito.when(customerRepo.findById(ID)).thenReturn(Optional.of(CUSTOMER));
        customerService.updateCustomerById(ID, updatedCustomer, ADDRESS);

        //Then
        Assertions.assertTrue(updatedCustomer.isActivated());
        Assertions.assertEquals(PASSWORD, updatedCustomer.getPassword());
        Mockito.verify(customerRepo, Mockito.times(1)).save(updatedCustomer);
    }

    @Test
    void updateCustomerByIdWhenPasswordsDontMatch() {
        //Given customer as originalCustomer
        List<Subscription> list = new ArrayList<>();
        list.add(SUBSCRIPTION);
        CUSTOMER.setSubscriptions(list);

        String newPass = "newPass";
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(2L);
        updatedCustomer.setPassword(newPass);
        updatedCustomer.setActivated(false);

        //When
        Mockito.when(customerRepo.findById(ID)).thenReturn(Optional.of(CUSTOMER));
        customerService.updateCustomerById(ID, updatedCustomer, ADDRESS);

        //Then
        Assertions.assertTrue(updatedCustomer.isActivated());
        Assertions.assertTrue(BCrypt.checkpw(newPass, updatedCustomer.getPassword()));
        Mockito.verify(customerRepo, Mockito.times(1)).save(updatedCustomer);
    }

    @Test
    void deleteCustomerById() {
        //Given customerId >= 0
        CART.setId(ID);
        CUSTOMER.setCart(CART);

        //When
        Mockito.when(customerRepo.findById(ID)).thenReturn(Optional.of(CUSTOMER));
        customerService.deleteCustomerById(ID);

        //Then
        Mockito.verify(cartService, Mockito.times(1)).deleteAllOrdersFromCart(CUSTOMER.getCart());
        Mockito.verify(customerRepo, Mockito.times(1)).deleteById(ID);
    }

    @Test
    void deleteCustomerByIdWhenIdIsZeroOrLess() {
        //Given
        Long invalidId = 0L;

        //When
        customerService.deleteCustomerById(invalidId);

        //Then
        Mockito.verifyNoInteractions(cartService);
        Mockito.verifyNoInteractions(customerRepo);
    }

    @Test
    void deleteCustomerByEmail() {
        //Given
        CART.setId(ID);
        CUSTOMER.setCart(CART);

        //When
        Mockito.when(customerRepo.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER));
        customerService.deleteCustomerByEmail(EMAIL);

        //Then
        Mockito.verify(cartService, Mockito.times(1)).deleteAllOrdersFromCart(CUSTOMER.getCart());
        Mockito.verify(customerRepo, Mockito.times(1)).deleteByEmail(EMAIL);
    }

    @Test
    void deleteCustomerByEmailWhenEmailIsNull() {
        //Given email is null
        //When
        customerService.deleteCustomerByEmail(null);

        //Then
        Mockito.verifyNoInteractions(cartService);
        Mockito.verifyNoInteractions(customerRepo);
    }

    @Test
    void activateUserShouldReturnTrue() {
        //Given correct activation code
        //When
        Mockito.when(customerRepo.findByActivationCode(ACTIVATION_CODE)).thenReturn(Optional.of(CUSTOMER));

        //Then
        Assertions.assertTrue(customerService.activateUser(ACTIVATION_CODE));
        Assertions.assertNull(CUSTOMER.getActivationCode());
        Assertions.assertTrue(CUSTOMER.isActivated());
        Mockito.verify(customerRepo, Mockito.times(1)).save(CUSTOMER);
    }

    @Test
    void activateUserShouldReturnFalseWhenActivationCodeIsNull() {
        //Given activation code is null
        //Then
        Assertions.assertFalse(customerService.activateUser(null));
        Mockito.verifyNoInteractions(customerRepo);
    }

    @Test
    void activateUserShouldReturnFalseWhenActivationCodesDoNotMatch() {
        //Given invalid activation code
        String invalidCode = "new activation code";

        //When
        Mockito.when(customerRepo.findByActivationCode(invalidCode)).thenReturn(Optional.empty());

        //Then
        Assertions.assertFalse(customerService.activateUser(invalidCode));
        Mockito.verifyNoMoreInteractions(customerRepo);

    }

    @Test
    void isSubscribedToArrivalShouldReturnTrue() {
        //Given
        Principal principal = () -> EMAIL;
        BOOK.setId(ID);
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(SUBSCRIPTION);
        CUSTOMER.setSubscriptions(subscriptions);

        //When
        Mockito.when(customerRepo.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER));

        //Then
        Assertions.assertTrue(customerService.isSubscribedToArrival(principal, BOOK));
    }

    @Test
    void isSubscribedToArrivalShouldReturnFalseWhenPrincipalOrBookIsNull() {
        //Given principal and book are null
        //Then
        Assertions.assertFalse(customerService.isSubscribedToArrival(null, null));
    }

    @Test
    void isSubscribedToArrivalShouldReturnFalseWhenCustomerDoNotHaveAnySubscriptions() {
        //Given
        Principal principal = () -> EMAIL;
        BOOK.setId(ID);

        //When
        Mockito.when(customerRepo.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER));

        //Then
        Assertions.assertFalse(customerService.isSubscribedToArrival(principal, BOOK));
    }

    @Test
    void isSubscribedToArrivalShouldReturnFalseWhenCustomerDoNotSubscribedToThisBook() {
        //Given
        Principal principal = () -> EMAIL;
        BOOK.setId(ID);
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(SUBSCRIPTION);
        CUSTOMER.setSubscriptions(subscriptions);
        Book anotherBook = new Book();
        anotherBook.setId(2L);

        //When
        Mockito.when(customerRepo.findByEmail(EMAIL)).thenReturn(Optional.of(CUSTOMER));

        //Then
        Assertions.assertFalse(customerService.isSubscribedToArrival(principal, anotherBook));
    }
}