package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.Order;
import com.mazanenko.petproject.bookshop.repository.OrderRepository;
import com.mazanenko.petproject.bookshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepo;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }


    @Override
    public void createOrder(Order order) {
        if (order != null) {
            orderRepo.save(order);
        }
    }

    @Override
    public Order readOrder(Long orderId) {
        if (orderId == null) {
            return null;
        }
        return orderRepo.findById(orderId).orElse(null);
    }

    @Override
    public Order readOrderByCartIdAndProductId(Long cartId, Long productId) {
        if (cartId == null || productId == null) {
            return null;
        }
        return orderRepo.findByCart_IdAndBook_Id(cartId, productId).orElse(null);
    }

    @Override
    public List<Order> readAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> readALLOrdersByCartId(Long cartId) {
        if (cartId == null) {
            return null;
        }
        return orderRepo.findAllByCart_Id(cartId);
    }

    @Override
    public void updateOrder(Long orderId, Order order) {
        if (orderId != null && order != null) {
            order.setId(orderId);
            orderRepo.save(order);
        }
    }

    @Override
    public void incrementOrderQuantity(Long orderId) {
        Order order = readOrder(orderId);

        if (order != null) {
            int quantity = order.getQuantity();
            order.setQuantity(++quantity);
            updateOrder(orderId, order);
        }
    }

    @Override
    public void decrementOrderQuantity(Long orderId) {
        Order order = readOrder(orderId);

        if (order != null) {
            int quantity = order.getQuantity();

            if (quantity > 1) {
                order.setQuantity(--quantity);
                updateOrder(orderId, order);
            } else {
                deleteOrder(orderId);
            }
        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        if (orderId != null) {
            orderRepo.deleteById(orderId);
        }
    }

    @Override
    public void deleteAllOrdersByCartId(Long cartId) {
        if (cartId != null) {
            orderRepo.deleteAllByCart_Id(cartId);
        }
    }
}
