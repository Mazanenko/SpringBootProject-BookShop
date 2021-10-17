package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.CartDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.Order;
import com.mazanenko.petproject.firstspringcrudapp.service.CartService;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import com.mazanenko.petproject.firstspringcrudapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final OrderService orderService;
    private final CustomerService customerService;

    @Autowired
    public CartServiceImpl(CartDAO cartDAO, OrderService orderService, CustomerService customerService) {
        this.cartDAO = cartDAO;
        this.orderService = orderService;
        this.customerService = customerService;
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
    public void addOrderToCartById(Order order) {
        orderService.createOrder(order);
    }

    @Override
    public void updateOrderInCartById(int orderId, Order order) {
        orderService.updateOrder(orderId, order);
    }

    @Override
    public void deleteOrderFromCartById(int orderId) {
        orderService.deleteOrder(orderId);
    }

    @Override
    public void deleteAllOrdersFromCart(int cartId) {
        orderService.deleteAllOrdersByCartId(cartId);
    }
}
