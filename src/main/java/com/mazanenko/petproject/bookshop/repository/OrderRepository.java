package com.mazanenko.petproject.bookshop.repository;

import com.mazanenko.petproject.bookshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCart_IdAndBook_Id(Long cartId, Long bookId);

    List<Order> findAllByCart_Id(Long cartId);

    void deleteAllByCart_Id(Long cartId);
}
