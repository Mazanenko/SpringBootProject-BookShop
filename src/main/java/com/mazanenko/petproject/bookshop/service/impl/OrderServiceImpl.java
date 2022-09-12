package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.Order;
import com.mazanenko.petproject.bookshop.repository.OrderRepository;
import com.mazanenko.petproject.bookshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepo;

    @Override
    public void createOrder(@NonNull Order order) {
        orderRepo.save(order);
        log.info("New order with product ({} with ID {}) was created for customer's cart with ID {}",
                order.getProduct().getName(), order.getProduct().getId(),
                order.getCart().getId());
    }

    @Override
    public Order readOrder(@NonNull Long orderId) {
        if (orderId < 1L) {
            log.warn("orderId can't be less than 1");
            return null;
        }
        return orderRepo.findById(orderId).orElse(null);
    }

    @Override
    public Order readOrderByCartIdAndProductId(@NonNull Long cartId, @NonNull Long productId) {
        if (cartId < 1 || productId < 1) {
            throw new IllegalArgumentException("cartId or productId can't be less than 1");
        }
        return orderRepo.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Can't find order with cartId: %d and productId: %d", cartId, productId)));
    }

    @Override
    public List<Order> readAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> readALLOrdersByCartId(@NonNull Long cartId) {
        if (cartId < 1) {
            log.warn("cartId can't be less than 1");
            return null;
        }
        return orderRepo.findAllByCart_Id(cartId);
    }

    @Override
    public void updateOrder(@NonNull Order updatedOrder) {
        if (updatedOrder.getId() < 1) {
            log.warn("orderId can't be less than 1");
            return;
        }
        orderRepo.save(updatedOrder);
    }

    @Override
    public void incrementOrderQuantity(@NonNull Order order) {
        if (order.getId() < 1) {
            log.warn("orderId can't be less than 1");
            return;
        }
        int quantity = order.getQuantity();
        order.setQuantity(++quantity);
        orderRepo.save(order);

        log.info("Increment quantity for order (ID {}) with product ({} with ID {}) " +
                        "for customer's cart with ID {}. Now quantity is {}", order.getId(), order.getProduct().getName(),
                order.getProduct().getId(), order.getCart().getId(), order.getQuantity());
    }

    @Override
    public void decrementOrderQuantity(@NonNull Order order) {
        if (order.getId() < 1) {
            log.warn("orderId can't be less than 1");
            return;
        }
        int quantity = order.getQuantity();
        if (quantity > 1) {
            order.setQuantity(--quantity);
            orderRepo.save(order);

            log.info("Decrement quantity for order (ID {}) with product ({} with ID {}) " +
                            "for customer's cart with ID {}. Now quantity is {}", order.getId(),
                    order.getProduct().getName(), order.getProduct().getId(),
                    order.getCart().getId(), order.getQuantity());
        } else {
            orderRepo.delete(order);
        }
    }

    @Override
    public void deleteOrder(@NonNull Order order) {
        if (order.getId() < 1) {
            log.warn("orderId can't be less than 1");
            return;
        }
        orderRepo.delete(order);
        log.info("The order (ID {}) with product ({} with ID {}) was deleted for customer's cart with ID {}",
                order.getId(), order.getProduct().getName(), order.getProduct().getId(),
                order.getCart().getId());
    }

    @Override
    public void deleteAllOrdersByCartId(@NonNull Long cartId) {
        if (cartId < 1) {
            log.warn("cartId can't be less than 1");
            return;
        }
        orderRepo.deleteAllByCart_Id(cartId);
        log.info("All orders was deleted for customer's cart with ID {}", cartId);
    }
}
