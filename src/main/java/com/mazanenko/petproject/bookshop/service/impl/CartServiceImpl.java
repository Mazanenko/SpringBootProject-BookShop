package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.annotation.LogException;
import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.Cart;
import com.mazanenko.petproject.bookshop.entity.Order;
import com.mazanenko.petproject.bookshop.entity.event.OrderEvent;
import com.mazanenko.petproject.bookshop.repository.CartRepository;
import com.mazanenko.petproject.bookshop.service.BookService;
import com.mazanenko.petproject.bookshop.service.CartService;
import com.mazanenko.petproject.bookshop.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final OrderService orderService;
    private final BookService bookService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final static Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    public CartServiceImpl(CartRepository cartRepo, OrderService orderService,
                           BookService bookService, ApplicationEventPublisher applicationEventPublisher) {
        this.cartRepo = cartRepo;
        this.orderService = orderService;
        this.bookService = bookService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Cart getCartById(Long cartId) {
        if (cartId == null) {
            return null;
        }
        return cartRepo.findById(cartId).orElse(null);
    }

    @Override
    public Cart getCartByCustomerId(Long customerId) {
        if (customerId == null) {
            return null;
        }
        return cartRepo.findByCustomer_Id(customerId).orElse(null);
    }

    @Override
    public Cart getCartByCustomerEmail(String email) {
        if (email == null) {
            return null;
        }
        return cartRepo.findByCustomer_Email(email).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void addToCart(Order order) throws SQLException {
        if (order == null) {
            return;
        }
        Order orderFromDatabase = orderService.readOrderByCartIdAndProductId(order.getCart().getId(),
                order.getBook().getId());

        if (orderFromDatabase == null) {
            orderService.createOrder(order);
        } else {
            orderService.incrementOrderQuantity(orderFromDatabase);
        }
        bookService.decrementBookQuantity(order.getBook().getId());
    }

    @Override
    public void addToCartByCustomerId(Long customerId, Long bookId) throws SQLException {
        if (customerId <= 0 || bookId <= 0) {
            return;
        }

        Cart cart = getCartByCustomerId(customerId);
        Book book = bookService.getBookById(bookId);
        if (cart == null || book == null) {
            return;
        }
        LOGGER.info("Manager adding book ({} by {} with ID {}) to customer's cart (ID {})", book.getName(),
                book.getAuthor(), book.getId(), cart.getId());

        Order order = new Order(cart, book, 1);
        addToCart(order);
    }

    @Override
    public void addToCartByCustomerEmail(String email, Long bookId) throws SQLException {
        if (email == null || bookId <= 0) {
            return;
        }
        Cart cart = getCartByCustomerEmail(email);
        Book book = bookService.getBookById(bookId);
        if (cart == null || book == null) {
            return;
        }
        LOGGER.info("Customer (email {}) adding book ({} by {} with ID {}) to cart", email, book.getName(),
                book.getAuthor(), book.getId());

        Order order = new Order(cart, book, 1);
        addToCart(order);
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void incrementProduct(Long productId, Cart cart) throws SQLException {
        if (productId <= 0 || cart == null) {
            return;
        }

        for (Order order : cart.getOrderList()) {
            if (order.getBook().getId().equals(productId)) {
                bookService.decrementBookQuantity(bookService.getBookById(productId).getId());
                orderService.incrementOrderQuantity(order);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void decrementProduct(Long productId, Cart cart) {
        if (productId <= 0 || cart == null) {
            return;
        }

        cart.getOrderList().forEach(order -> {
            if (order.getBook().getId().equals(productId)) {
                orderService.decrementOrderQuantity(order);
                bookService.incrementBookQuantity(bookService.getBookById(productId).getId());
            }
        });
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void deleteOrderFromCart(Long productId, Cart cart) {
        if (productId <= 0 || cart == null) {
            return;
        }

        Book book = bookService.getBookById(productId);
        if (book == null) {
            return;
        }

        cart.getOrderList().forEach(order -> {
            if (order.getBook().getId().equals(productId)) {
                int newQuantity = book.getAvailableQuantity() + order.getQuantity();

                orderService.deleteOrder(order);
                bookService.setQuantity(productId, newQuantity);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogException
    public void deleteAllOrdersFromCart(Cart cart) {
        if (cart == null) {
            return;
        }

        cart.getOrderList().forEach(order -> {
            Book tempBook = order.getBook();
            int newQuantity = order.getQuantity() + tempBook.getAvailableQuantity();

            bookService.setQuantity(tempBook.getId(), newQuantity);
        });
        orderService.deleteAllOrdersByCartId(cart.getId());
    }

    @Override
    @Transactional
    public void makeAnOrderByCustomerEmail(String email) {
        if (email == null) {
            return;
        }

        Cart cart = getCartByCustomerEmail(email);

        if (cart == null) {
            return;
        }

        publishOrderEvent(cart);
        orderService.deleteAllOrdersByCartId(cart.getId());

        List<String> orderList = new ArrayList<>();
        cart.getOrderList().forEach(order -> orderList.add(order.getBook().getName() +
                " by " + order.getBook().getAuthor() + " - " + order.getQuantity() + " pc."));

        LOGGER.info("Customer (email {}) made the order {}", email, orderList);
    }


    private void publishOrderEvent(Cart cart) {
        if (cart == null) {
            return;
        }
        applicationEventPublisher.publishEvent(new OrderEvent(cart));
    }
}
