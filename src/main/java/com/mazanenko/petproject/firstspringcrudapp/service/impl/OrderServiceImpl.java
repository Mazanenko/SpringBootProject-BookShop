package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.OrderDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Order;
import com.mazanenko.petproject.firstspringcrudapp.service.BookService;
import com.mazanenko.petproject.firstspringcrudapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO;
    private final BookService bookService;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO, BookService bookService) {
        this.orderDAO = orderDAO;
        this.bookService = bookService;
    }


    @Override
    public void createOrder(Order order) {
        orderDAO.create(order);
    }

    @Override
    public Order readOrder(int id) {
        return orderDAO.read(id);
    }

    @Override
    public Order readOrderByCartIdAndProductId(int cartId, int productId) {
        return orderDAO.readOrderByCartIdAndProductId(cartId, productId);
    }

    @Override
    public List<Order> readAllOrders() {
        return orderDAO.readAll();
    }

    @Override
    public List<Order> readALLOrdersByCartId(int cartId) {
        List<Order> orders = orderDAO.readALLByCartId(cartId);
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                order.setBook(bookService.getBookById(order.getProductId()));
            }
        }
        return orders;
    }

    @Override
    public void updateOrder(int id, Order order) {
        orderDAO.update(id, order);
    }

    @Override
    public void incrementOrderQuantity(int orderId) {
        Order order = readOrder(orderId);
        int quantity = order.getQuantity();
        order.setQuantity(++quantity);
        updateOrder(orderId, order);
    }

    @Override
    public void decrementOrderQuantity(int orderId) {
        Order order = readOrder(orderId);
        int quantity = order.getQuantity();

        if (quantity > 1) {
            order.setQuantity(--quantity);
            updateOrder(orderId, order);
        } else {
            deleteOrder(orderId);
        }
    }

    @Override
    public void deleteOrder(int id) {
        orderDAO.delete(id);
    }

    @Override
    public void deleteAllOrdersByCartId(int cartId) {
        orderDAO.deleteAllByCartId(cartId);
    }
}
