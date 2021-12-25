package com.mazanenko.petproject.bookshop.repository;

import com.mazanenko.petproject.bookshop.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByBook_IdAndCustomer_Id(Long bookId, Long customerId);
}
