package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.Order;
import com.mazanenko.petproject.bookshop.repository.OrderRepository;
import com.mazanenko.petproject.bookshop.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepo;
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl() {
    }

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }


    @Override
    public void createOrder(Order order) {
        if (order == null) {
            return;
        }
        orderRepo.save(order);

        LOGGER.info("New order with book ({} by {} with ID {}) was created for customer's cart with ID {}",
                order.getBook().getName(), order.getBook().getAuthor(), order.getBook().getId(),
                order.getCart().getId());
    }

    @Override
    public Order readOrder(Long orderId) {
        if (orderId <= 0) {
            return null;
        }
        return orderRepo.findById(orderId).orElse(null);
    }

    @Override
    public Order readOrderByCartIdAndProductId(Long cartId, Long productId) {
        if (cartId <= 0 || productId <= 0) {
            return null;
        }
        return orderRepo.findByCart_IdAndBook_Id(cartId, productId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> readAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> readALLOrdersByCartId(Long cartId) {
        if (cartId <= 0) {
            return null;
        }
        return orderRepo.findAllByCart_Id(cartId);
    }

    @Override
    public void updateOrder(Order updatedOrder) {
        if (updatedOrder == null || updatedOrder.getId() <= 0) {
            return;
        }
        orderRepo.save(updatedOrder);
    }

    @Override
    public void incrementOrderQuantity(Order order) {
        if (order == null || order.getId() <= 0) {
            return;
        }

        int quantity = order.getQuantity();
        order.setQuantity(++quantity);
        updateOrder(order);

        LOGGER.info("Increment quantity for order (ID {}) with book ({} by {} with ID {}) " +
                        "for customer's cart with ID {}. Now quantity is {}", order.getId(), order.getBook().getName(),
                order.getBook().getAuthor(), order.getBook().getId(), order.getCart().getId(), order.getQuantity());
    }

    @Override
    public void decrementOrderQuantity(Order order) {
        if (order == null || order.getId() <= 0) {
            return;
        }
        int quantity = order.getQuantity();

        if (quantity > 1) {
            order.setQuantity(--quantity);
            updateOrder(order);

            LOGGER.info("Decrement quantity for order (ID {}) with book ({} by {} with ID {}) " +
                            "for customer's cart with ID {}. Now quantity is {}", order.getId(),
                    order.getBook().getName(), order.getBook().getAuthor(), order.getBook().getId(),
                    order.getCart().getId(), order.getQuantity());
        } else {
            deleteOrder(order);
        }
    }

    @Override
    public void deleteOrder(Order order) {
        if (order == null || order.getId() <= 0) {
            return;
        }
        orderRepo.delete(order);

        LOGGER.info("The order (ID {}) with book ({} by {} with ID {}) was deleted for customer's cart with ID {}",
                order.getId(), order.getBook().getName(), order.getBook().getAuthor(), order.getBook().getId(),
                order.getCart().getId());
    }

    @Override
    public void deleteAllOrdersByCartId(Long cartId) {
        if (cartId <= 0) {
            return;
        }
        orderRepo.deleteAllByCart_Id(cartId);

        LOGGER.info("All orders was deleted for customer's cart with ID {}", cartId);
    }
}
