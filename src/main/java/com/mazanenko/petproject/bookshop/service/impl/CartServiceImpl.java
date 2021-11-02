package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.dao.CartDAO;
import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.Cart;
import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.Order;
import com.mazanenko.petproject.bookshop.entity.event.OrderEvent;
import com.mazanenko.petproject.bookshop.service.BookService;
import com.mazanenko.petproject.bookshop.service.CartService;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import com.mazanenko.petproject.bookshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final BookService bookService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public CartServiceImpl(CartDAO cartDAO, OrderService orderService, CustomerService customerService,
                           BookService bookService, ApplicationEventPublisher applicationEventPublisher) {
        this.cartDAO = cartDAO;
        this.orderService = orderService;
        this.customerService = customerService;
        this.bookService = bookService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Cart getCartById(int cartId) {
        Cart cart = cartDAO.read(cartId);
        if (cart != null) {
            cart.setOrderList(orderService.readALLOrdersByCartId(cart.getId()));
        }
        return cart;
    }

    @Override
    public Cart getCartByCustomerId(int id) {
        Cart cart = cartDAO.readByCustomerId(id);
        if (cart != null) {
            cart.setOrderList(orderService.readALLOrdersByCartId(cart.getId()));
        }
        return cart;
    }

    @Override
    public Cart getCartByCustomerEmail(String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        return getCartByCustomerId(customer.getId());
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void addToCart(Order order) throws SQLException {
        Order tempOrder = orderService.readOrderByCartIdAndProductId(order.getCartId(), order.getProductId());

        if (tempOrder == null) {
            orderService.createOrder(order);
        } else {
            orderService.incrementOrderQuantity(tempOrder.getId());
        }
        bookService.decrementBookQuantity(order.getProductId());
    }

    @Override
    public void addToCartByCustomerId(int customerId, int bookId) throws SQLException {
        Cart cart = getCartByCustomerId(customerId);
        Order order = new Order(cart.getId(), bookId, 1);
        addToCart(order);
    }

    @Override
    public void addToCartByCustomerEmail(String email, int bookId) throws SQLException {
        Cart cart = getCartByCustomerEmail(email);
        Order order = new Order(cart.getId(), bookId, 1);
        addToCart(order);
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void incrementProduct(int productId, Cart cart) throws SQLException {

        for (Order order : cart.getOrderList()) {
            if (order.getProductId() == productId) {

                bookService.decrementBookQuantity(bookService.getBookById(productId).getId());
                orderService.incrementOrderQuantity(order.getId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void decrementProduct(int productId, Cart cart) {

        cart.getOrderList().forEach(order -> {
            if (order.getProductId() == productId) {
                orderService.decrementOrderQuantity(order.getId());
                bookService.incrementBookQuantity(bookService.getBookById(productId).getId());
            }
        });
    }

    @Override
    public void updateOrderInCartById(int orderId, Order order) {
        orderService.updateOrder(orderId, order);
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void deleteOrderFromCart(int productId, Cart cart) {
        Book book = bookService.getBookById(productId);

        cart.getOrderList().forEach(order -> {
            if (order.getProductId() == productId) {
                int newQuantity = book.getAvailableQuantity() + order.getQuantity();

                orderService.deleteOrder(order.getId());
                bookService.setQuantity(productId, newQuantity);
            }
        });
    }

    @Override
    public void deleteAllOrdersFromCart(int cartId) {
        orderService.deleteAllOrdersByCartId(cartId);
    }

    @Override
    public void makeAnOrderByCustomerEmail(String email) {
        Cart cart = getCartByCustomerEmail(email);

        deleteAllOrdersFromCart(cart.getId());

        new Thread(() -> publishOrderEvent(cart)).start();
    }


    private void publishOrderEvent(Cart cart) {
        applicationEventPublisher.publishEvent(new OrderEvent(cart));
    }
}
