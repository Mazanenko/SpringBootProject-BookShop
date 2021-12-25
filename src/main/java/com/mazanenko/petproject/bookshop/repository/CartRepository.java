package com.mazanenko.petproject.bookshop.repository;

import com.mazanenko.petproject.bookshop.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "orderList",
                    "orderList.book"
            })
    Optional<Cart> findByCustomer_Id(Long customerId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "orderList",
                    "orderList.book"
            })
    Optional<Cart> findByCustomer_Email(String email);

    @Override
    @NonNull
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"orderList"})
    Optional<Cart> findById(@NonNull Long aLong);
}
