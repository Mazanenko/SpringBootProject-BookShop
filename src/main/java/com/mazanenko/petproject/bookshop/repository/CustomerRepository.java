package com.mazanenko.petproject.bookshop.repository;

import com.mazanenko.petproject.bookshop.entity.Customer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @NonNull
    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "deliveryAddress",
                    "cart"
            })
    List<Customer> findAll(@NonNull Sort sort);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByActivationCode(String code);

    void deleteByEmail(String email);
}
