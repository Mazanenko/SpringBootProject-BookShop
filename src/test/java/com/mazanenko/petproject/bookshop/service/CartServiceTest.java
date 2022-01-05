package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.*;
import com.mazanenko.petproject.bookshop.repository.CartRepository;
import com.mazanenko.petproject.bookshop.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    CartService cartService = Mockito.spy(new CartServiceImpl());

    @Mock
    private CartRepository cartRepo;
    @Mock
    private OrderService orderService;
    @Mock
    private BookService bookService;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;


    private final Book BOOK = new Book(1L, "name", "description", 3, 5,
            "author", "genre", "url");
    private final Cart CART = new Cart();
    private final Order ORDER = new Order(CART, BOOK, 3);
    private final DeliveryAddress DELIVERY_ADDRESS = new DeliveryAddress(1L, "country", "city",
            "street", 1, "note");

    private final Customer CUSTOMER = new Customer(1L, "Name", "Surname", "male",
            "12345", "test_customer@mail.ru", "password", DELIVERY_ADDRESS, CART);


    @BeforeEach
    void setUp() {
        CART.setId(1L);
        CART.setCustomer(CUSTOMER);
        ORDER.setId(1L);
    }

    @Test
    void getCartByIdShouldReturnCart() {
        //Given cartId
        //When
        cartService.getCartById(CART.getId());

        //Then
        Mockito.verify(cartRepo, Mockito.times(1)).findById(CART.getId());
    }

    @Test
    void getCartByIdShouldNotReturnCartWhenCartIdLessThanOne() {
        //Given cartId = 0
        CART.setId(0L);

        //When
        cartService.getCartById(CART.getId());

        //Then
        Mockito.verifyNoInteractions(cartRepo);
    }

    @Test
    void getCartByCustomerIdShouldReturnCart() {
        //Given customerId
        //When
        cartService.getCartByCustomerId(CUSTOMER.getId());

        //Then
        Mockito.verify(cartRepo, Mockito.times(1)).findByCustomer_Id(CUSTOMER.getId());
    }

    @Test
    void getCartByCustomerIdShouldNotReturnCartWhenCustomerIdLessThanOne() {
        //Given customerId = 0
        CUSTOMER.setId(0L);

        //When
        cartService.getCartByCustomerId(CUSTOMER.getId());

        //Then
        Mockito.verifyNoInteractions(cartRepo);
    }

    @Test
    void getCartByCustomerEmailShouldReturnCart() {
        //Given customerEmail
        //When
        cartService.getCartByCustomerEmail(CUSTOMER.getEmail());

        //Then
        Mockito.verify(cartRepo, Mockito.times(1)).findByCustomer_Email(CUSTOMER.getEmail());
    }

    @Test
    void getCartByCustomerEmailShouldNotReturnCartWhenCustomerEmailIsNull() {
        //Given customerEmail = null
        //When
        cartService.getCartByCustomerEmail(null);

        //Then
        Mockito.verifyNoInteractions(cartRepo);
    }

    @Test
    void addToCartShouldAddNewOrderToCart() throws SQLException {
        //Given order
        //When
        Mockito.when(orderService.readOrderByCartIdAndProductId(ORDER.getCart().getId(),
                ORDER.getBook().getId())).thenReturn(null);
        cartService.addToCart(ORDER);

        //Then
        Mockito.verify(orderService, Mockito.times(1)).createOrder(ORDER);
        Mockito.verify(bookService, Mockito.times(1)).decrementBookQuantity(ORDER.getBook());
    }

    @Test
    void addToCartShouldIncrementOrderWhenItIsAlreadyInCart() throws SQLException {
        //Given order
        //When
        Mockito.when(orderService.readOrderByCartIdAndProductId(ORDER.getCart().getId(),
                ORDER.getBook().getId())).thenReturn(ORDER);
        cartService.addToCart(ORDER);

        //Then
        Mockito.verify(orderService, Mockito.times(1)).incrementOrderQuantity(ORDER);
        Mockito.verify(bookService, Mockito.times(1)).decrementBookQuantity(ORDER.getBook());
    }

    @Test
    void addToCartShouldNotAddNewOrderToCartOrIncrementExistOrderWhenOrderIsNull() throws SQLException {
        //Given order = null
        //When
        cartService.addToCart(null);

        //Then
        Mockito.verifyNoInteractions(orderService);
        Mockito.verifyNoInteractions(bookService);
    }

    @Test
    void addToCartByCustomerIdShouldAddNewOrderToCart() throws SQLException {
        //Given customerId and bookId
        //When
        Mockito.when(cartRepo.findByCustomer_Id(CUSTOMER.getId())).thenReturn(Optional.of(CART));
        Mockito.when(bookService.getBookById(BOOK.getId())).thenReturn(BOOK);
        Mockito.when(orderService.readOrderByCartIdAndProductId(ORDER.getCart().getId(), ORDER.getBook().getId()))
                .thenReturn(null);
        cartService.addToCartByCustomerId(CUSTOMER.getId(), BOOK.getId());

        //Then
        Mockito.verify(cartService, Mockito.times(1)).getCartByCustomerId(CUSTOMER.getId());
        Mockito.verify(orderService, Mockito.times(1)).createOrder(Mockito.any(Order.class));
        Mockito.verify(bookService, Mockito.times(1)).decrementBookQuantity(ORDER.getBook());
    }

    @Test
    void addToCartByCustomerIdShouldIncrementOrderWhenItIsAlreadyInCart() throws SQLException {
        //Given customerId and bookId
        //When
        Mockito.when(cartRepo.findByCustomer_Id(CUSTOMER.getId())).thenReturn(Optional.of(CART));
        Mockito.when(bookService.getBookById(BOOK.getId())).thenReturn(BOOK);
        Mockito.when(orderService.readOrderByCartIdAndProductId(ORDER.getCart().getId(), ORDER.getBook().getId()))
                .thenReturn(ORDER);
        cartService.addToCartByCustomerId(CUSTOMER.getId(), BOOK.getId());

        //Then
        Mockito.verify(cartService, Mockito.times(1)).getCartByCustomerId(CUSTOMER.getId());
        Mockito.verify(orderService, Mockito.times(1)).incrementOrderQuantity(ORDER);
        Mockito.verify(bookService, Mockito.times(1)).decrementBookQuantity(ORDER.getBook());
    }

    @Test
    void addToCartByCustomerIdShouldNotAddNewOrderToCartOrIncrementExistOrderWhenCustomerIdOrBookIdIsLessThanOne()
            throws SQLException {
        //Given customerId = 0 and bookId = 0
        //When
        cartService.addToCartByCustomerId(0L, 0L);

        //Then
        Mockito.verifyNoInteractions(cartRepo);
        Mockito.verifyNoInteractions(orderService);
        Mockito.verifyNoInteractions(bookService);
    }

    @Test
    void addToCartByCustomerEmailShouldAddNewOrderToCart() throws SQLException {
        //Given customerEmail and bookId
        //When
        Mockito.when(cartRepo.findByCustomer_Email(CUSTOMER.getEmail())).thenReturn(Optional.of(CART));
        Mockito.when(bookService.getBookById(BOOK.getId())).thenReturn(BOOK);
        Mockito.when(orderService.readOrderByCartIdAndProductId(ORDER.getCart().getId(), ORDER.getBook().getId()))
                .thenReturn(null);
        cartService.addToCartByCustomerEmail(CUSTOMER.getEmail(), BOOK.getId());

        //Then
        Mockito.verify(cartService, Mockito.times(1)).getCartByCustomerEmail(CUSTOMER.getEmail());
        Mockito.verify(orderService, Mockito.times(1)).createOrder(Mockito.any(Order.class));
        Mockito.verify(bookService, Mockito.times(1)).decrementBookQuantity(ORDER.getBook());
    }

    @Test
    void addToCartByCustomerEmailShouldIncrementOrderWhenItIsAlreadyInCart() throws SQLException {
        //Given customerEmail and bookId
        //When
        Mockito.when(cartRepo.findByCustomer_Email(CUSTOMER.getEmail())).thenReturn(Optional.of(CART));
        Mockito.when(bookService.getBookById(BOOK.getId())).thenReturn(BOOK);
        Mockito.when(orderService.readOrderByCartIdAndProductId(ORDER.getCart().getId(), ORDER.getBook().getId()))
                .thenReturn(ORDER);
        cartService.addToCartByCustomerEmail(CUSTOMER.getEmail(), BOOK.getId());

        //Then
        Mockito.verify(cartService, Mockito.times(1)).getCartByCustomerEmail(CUSTOMER.getEmail());
        Mockito.verify(orderService, Mockito.times(1)).incrementOrderQuantity(ORDER);
        Mockito.verify(bookService, Mockito.times(1)).decrementBookQuantity(ORDER.getBook());
    }

    @Test
    void addToCartByCustomerEmailShouldNotAddNewOrderToCartOrIncrementExistOrderWhenEmailIsNullOrBookIdIsLessThanOne()
            throws SQLException {
        //Given customerEmail = null and bookId = 0
        //When
        cartService.addToCartByCustomerEmail(null, 0L);

        //Then
        Mockito.verifyNoInteractions(cartRepo);
        Mockito.verifyNoInteractions(orderService);
        Mockito.verifyNoInteractions(bookService);
    }

    @Test
    void incrementProductShouldIncrementProductInCart() throws SQLException {
        //Given bookId and cart
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        //When
        cartService.incrementProduct(BOOK.getId(), CART);

        //Then
        Mockito.verify(bookService, Mockito.times(1)).decrementBookQuantity(BOOK);
        Mockito.verify(orderService, Mockito.times(1)).incrementOrderQuantity(ORDER);
    }

    @Test
    void incrementProductShouldNotIncrementProductInCartWhenOrderListDoesNotContainsBook() throws SQLException {
        //Given another bookId and cart
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        //When
        cartService.incrementProduct(2L, CART);

        //Then
        Mockito.verifyNoInteractions(bookService);
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void incrementProductShouldNotIncrementProductInCartWhenBookIdLessThanOneOrCartIsNull() throws SQLException {
        //Given bookId = 0 and cart = null
        //When
        cartService.incrementProduct(0L, null);

        //Then
        Mockito.verifyNoInteractions(bookService);
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void decrementProductShouldDecrementProductInCart() {
        //Given bookId and cart
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        //When
        cartService.decrementProduct(BOOK.getId(), CART);

        //Then
        Mockito.verify(bookService, Mockito.times(1)).incrementBookQuantity(BOOK);
        Mockito.verify(orderService, Mockito.times(1)).decrementOrderQuantity(ORDER);
    }

    @Test
    void decrementProductShouldNotDecrementProductInCartWhenOrderListDoesNotContainsBook() {
        //Given another bookId and cart
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        //When
        cartService.decrementProduct(2L, CART);

        //Then
        Mockito.verifyNoInteractions(bookService);
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void decrementProductShouldNotDecrementProductInCartWhenBookIdLessThanOneOrCartIsNull() {
        //Given bookId = 0 and cart = null
        //When
        cartService.decrementProduct(0L, null);

        //Then
        Mockito.verifyNoInteractions(bookService);
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void deleteOrderFromCartShouldDeleteOrderFromCart() {
        //Given productId and cart
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        int newQuantity = BOOK.getAvailableQuantity() + ORDER.getQuantity();

        //When
        cartService.deleteOrderFromCart(BOOK.getId(), CART);

        //Then
        Mockito.verify(orderService, Mockito.times(1)).deleteOrder(ORDER);
        Mockito.verify(bookService, Mockito.times(1)).setQuantity(BOOK.getId(), newQuantity);
    }

    @Test
    void deleteOrderFromCartShouldNotDeleteOrderFromCartWhenOrderListDoesNotContainsBook() {
        //Given productId and cart
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        //When
        cartService.deleteOrderFromCart(2L, CART);

        //Then
        Mockito.verifyNoInteractions(bookService);
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void deleteOrderFromCartShouldNotDeleteOrderFromCartWhenBookIdLessThanOneOrCartIsNull() {
        //Given bookId = 0 and cart = nul
        //When
        cartService.deleteOrderFromCart(0L, null);

        //Then
        Mockito.verifyNoInteractions(bookService);
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void deleteAllOrdersFromCartShouldDeleteAllOrdersFromCartWhenOrderListContainsAnOrder() {
        //Given cart
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        int newQuantity = BOOK.getAvailableQuantity() + ORDER.getQuantity();

        //When
        cartService.deleteAllOrdersFromCart(CART);

        //Then
        Mockito.verify(bookService, Mockito.times(1)).setQuantity(BOOK.getId(), newQuantity);
        Mockito.verify(orderService, Mockito.times(1)).deleteAllOrdersByCartId(CART.getId());
    }

    @Test
    void deleteAllOrdersFromCartShouldDeleteAllOrdersFromCartWhenOrderListDoesNotContainsAnyOrder() {
        //Given cart
        List<Order> orderList = new ArrayList<>();
        CART.setOrderList(orderList);

        //When
        cartService.deleteAllOrdersFromCart(CART);

        //Then
        Mockito.verifyNoInteractions(bookService);
        Mockito.verify(orderService, Mockito.times(1)).deleteAllOrdersByCartId(CART.getId());
    }

    @Test
    void deleteAllOrdersFromCartShouldNotDeleteAllOrdersFromCartWhenCartIsNull() {
        //Given cart = null
        //When
        cartService.deleteAllOrdersFromCart(null);

        //Then
        Mockito.verifyNoInteractions(bookService);
        Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void makeAnOrderByCustomerEmailShouldMakeAnOrder() {
        //Given customerEmail
        //When
        Mockito.when(cartRepo.findByCustomer_Email(CUSTOMER.getEmail())).thenReturn(Optional.of(CART));
        cartService.makeAnOrderByCustomerEmail(CUSTOMER.getEmail());
        ArgumentCaptor<ApplicationEventPublisher> captor = ArgumentCaptor
                .forClass(ApplicationEventPublisher.class);

        //Then
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(captor.capture());

        Mockito.verify(orderService, Mockito.times(1)).deleteAllOrdersByCartId(CART.getId());
    }

    @Test
    void makeAnOrderByCustomerEmailShouldNotMakeAnOrderWhenEmailIsNull() {
        //Given customerEmail = null
        //When
        cartService.makeAnOrderByCustomerEmail(null);

        //Then
        Mockito.verifyNoInteractions(cartRepo);
        Mockito.verifyNoInteractions(applicationEventPublisher);
        Mockito.verifyNoInteractions(orderService);
    }
}