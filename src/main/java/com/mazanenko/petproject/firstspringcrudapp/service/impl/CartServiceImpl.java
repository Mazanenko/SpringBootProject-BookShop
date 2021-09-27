package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.impl.CartDAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.impl.OrderDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Order;
import com.mazanenko.petproject.firstspringcrudapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final OrderDAO orderDAO;

    @Autowired
    public CartServiceImpl(CartDAO cartDAO, OrderDAO orderDAO) {
        this.cartDAO = cartDAO;
        this.orderDAO = orderDAO;
    }

    @Override
    public Cart getCartById(int cartId) {
        Cart cart = cartDAO.read(cartId);
        if (cart != null) {
            cart.setOrderList(orderDAO.readALLByCartId(cartId));
        }
        return cart;
    }

    @Override
    public void addOrderToCartById(Order order) {
        orderDAO.create(order);
    }

    @Override
    public void updateOrderInCartById(int orderId, Order order) {
        orderDAO.update(orderId, order);
    }

    @Override
    public void deleteOrderFromCartById(int orderId) {
        orderDAO.delete(orderId);
    }

    @Override
    public void deleteAllOrdersFromCart(int cartId) {
        orderDAO.deleteAll(cartId);
    }
}
