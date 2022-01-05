package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.Cart;
import com.mazanenko.petproject.bookshop.entity.Order;
import com.mazanenko.petproject.bookshop.repository.OrderRepository;
import com.mazanenko.petproject.bookshop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();

    @Mock
    private OrderRepository orderRepo;


    private final Long ORDER_ID = 1L;
    private int orderQuantity = 3;
    private final Book BOOK = new Book(1L, "name", "description", 3, 5,
            "author", "genre", "url");
    private final Cart CART = new Cart();
    private final Order ORDER = new Order(CART, BOOK, orderQuantity);

    @BeforeEach
    void setUp() {
        CART.setId(1L);
        ORDER.setId(ORDER_ID);
    }

    @Test
    void createOrderShouldCreateNewOrder() {
        //Given order
        //When
        orderService.createOrder(ORDER);

        //Then
        Mockito.verify(orderRepo, Mockito.times(1)).save(ORDER);
    }

    @Test
    void createOrderWhenOrderIsNull() {
        //Given order is null
        //When
        orderService.createOrder(null);

        //Then
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void readOrderShouldReturnOrder() {
        //Given order's id
        //When
        Mockito.when(orderRepo.findById(ORDER_ID)).thenReturn(Optional.of(ORDER));

        //Then
        Assertions.assertEquals(ORDER, orderService.readOrder(ORDER_ID));
    }

    @Test
    void readOrderShouldReturnNullWhenIdLessThanOne() {
        //Given order's id = 0

        //Then
        Assertions.assertNull(orderService.readOrder(0L));
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void readOrderByCartIdAndProductIdShouldReturnOrder() {
        //Given cartId and productId
        //When
        Mockito.when(orderRepo.findByCart_IdAndBook_Id(CART.getId(), BOOK.getId())).thenReturn(Optional.of(ORDER));

        //Then
        Assertions.assertEquals(ORDER, orderService.readOrderByCartIdAndProductId(CART.getId(), BOOK.getId()));
    }

    @Test
    void readOrderByCartIdAndProductIdWhenCartIdOrBookIdIsLessThanOne() {
        //Given cartId = 0 and productId = 0

        //Then
        Assertions.assertNull(orderService.readOrderByCartIdAndProductId(0L, 0L));
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void readAllOrdersShouldReturnListOfOrders() {
        //When
        orderService.readAllOrders();
        //Then
        Mockito.verify(orderRepo, Mockito.times(1)).findAll();
    }

    @Test
    void readALLOrdersByCartIdShouldReturnListOfOrders() {
        //Given cartId
        //When
        orderService.readALLOrdersByCartId(CART.getId());

        //Then
        Mockito.verify(orderRepo, Mockito.times(1)).findAllByCart_Id(CART.getId());
    }

    @Test
    void readALLOrdersByCartIdShouldReturnNullWhenCartIdLessThanOne() {
        //Given cartId = 0
        //Then
        Assertions.assertNull(orderService.readALLOrdersByCartId(0L));
    }

    @Test
    void updateOrderShouldUpdateOrder() {
        //Given
        Order updatedOrder = new Order(CART, BOOK, 2);
        updatedOrder.setId(ORDER_ID);

        //When
        orderService.updateOrder(updatedOrder);

        //Then
        Mockito.verify(orderRepo, Mockito.times(1)).save(updatedOrder);
    }

    @Test
    void updateOrderShouldNotUpdateOrderWhenUpdateOrderIdLessThanOne() {
        //Given orderId = 0
        Order updatedOrder = new Order(CART, BOOK, 2);
        updatedOrder.setId(0L);

        //When
        orderService.updateOrder(updatedOrder);

        //Then
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void updateOrderShouldNotUpdateOrderWhenUpdateOrderIsNull() {
        //Given updatedOrder = null
        //When
        orderService.updateOrder(null);

        //Then
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void incrementOrderQuantityShouldIncrementOrderQuantity() {
        //Given order
        //When
        orderService.incrementOrderQuantity(ORDER);

        //Then
        Assertions.assertEquals(++orderQuantity, ORDER.getQuantity());
        Mockito.verify(orderRepo, Mockito.times(1)).save(ORDER);
    }

    @Test
    void incrementOrderQuantityShouldNotIncrementOrderQuantityWhenOrderIdLessThanOne() {
        //Given orderId = 0
        ORDER.setId(0L);
        //When
        orderService.incrementOrderQuantity(ORDER);

        //Then
        Assertions.assertEquals(orderQuantity, ORDER.getQuantity());
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void incrementOrderQuantityShouldNotIncrementOrderQuantityWhenOrderIsNull() {
        //Given order = null
        //When
        orderService.incrementOrderQuantity(null);

        //Then
        Assertions.assertEquals(orderQuantity, ORDER.getQuantity());
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void decrementOrderQuantityShouldDecrementOrderQuantity() {
        //Given order
        //When
        orderService.decrementOrderQuantity(ORDER);

        //Then
        Assertions.assertEquals(--orderQuantity, ORDER.getQuantity());
        Mockito.verify(orderRepo, Mockito.times(1)).save(ORDER);
    }

    @Test
    void decrementOrderQuantityShouldNotDecrementOrderQuantityWhenOrderIdLessThanOne() {
        //Given orderId = 0
        ORDER.setId(0L);
        //When
        orderService.decrementOrderQuantity(ORDER);

        //Then
        Assertions.assertEquals(orderQuantity, ORDER.getQuantity());
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void decrementOrderQuantityShouldNotDecrementOrderQuantityWhenOrderIsNull() {
        //Given order = null
        //When
        orderService.decrementOrderQuantity(null);

        //Then
        Assertions.assertEquals(orderQuantity, ORDER.getQuantity());
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void decrementOrderQuantityShouldDeleteOrderWhenOrderQuantityIsOneOrLess() {
        //Given orderQuantity = 1
        ORDER.setQuantity(1);
        //When
        orderService.decrementOrderQuantity(ORDER);

        //Then
        Mockito.verify(orderRepo, Mockito.times(1)).delete(ORDER);
        Mockito.verifyNoMoreInteractions(orderRepo);
    }

    @Test
    void deleteOrderShouldDeleteOrder() {
        //Given order
        //When
        orderService.deleteOrder(ORDER);

        //Then
        Mockito.verify(orderRepo, Mockito.times(1)).delete(ORDER);
    }

    @Test
    void deleteOrderShouldNotDeleteOrderWhenOrderIdLessThanOne() {
        //Given orderID = 0
        ORDER.setId(0L);
        //When
        orderService.deleteOrder(ORDER);

        //Then
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void deleteOrderShouldNotDeleteOrderWhenOrderIsNull() {
        //Given order = null
        //When
        orderService.deleteOrder(null);

        //Then
        Mockito.verifyNoInteractions(orderRepo);
    }

    @Test
    void deleteAllOrdersByCartIdShouldDeleteAllOrdersFromCart() {
        //Given cartId
        //When
        orderService.deleteAllOrdersByCartId(CART.getId());

        //Then
        Mockito.verify(orderRepo, Mockito.times(1)).deleteAllByCart_Id(CART.getId());
    }

    @Test
    void deleteAllOrdersByCartIdShouldNotDeleteAllOrdersFromCartWhenCartIdLessThanOne() {
        //Given cartId = 0
        CART.setId(0L);
        //When
        orderService.deleteAllOrdersByCartId(CART.getId());

        //Then
        Mockito.verifyNoInteractions(orderRepo);
    }
}